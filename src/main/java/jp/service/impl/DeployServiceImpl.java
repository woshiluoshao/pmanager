package jp.service.impl;

import jp.entity.DeployEntity;
import jp.entity.DeployTask;
import jp.enums.MessageEnum;
import jp.service.IDeployService;
import jp.utils.*;
import jp.vo.ResultVo;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;

@Service
public class DeployServiceImpl implements IDeployService {

    @Autowired
    Environment env;

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    private static final String PROCESS_DEFINE_KEY = "deployProc";


    @Override
    public ResultVo addDeploy(HttpServletRequest request) {

        try {
            //获取参数
            String sender = request.getParameter("sender");
            String receiver = request.getParameter("receiver");
            String environment = request.getParameter("environment");
            String readme = request.getParameter("readme");

            String fileSavePath = env.getProperty("manager.save-path");

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 获取上传的文件
            MultipartFile multiFile = multipartRequest.getFile("file");

            assert multiFile != null;
            String fileName = multiFile.getOriginalFilename();
            multiFile.getContentType();
            InputStream inputStream = multiFile.getInputStream();
            String totalPath = fileSavePath + fileName;
            FIleUtils.createFolder(fileSavePath);
            boolean result = FIleUtils.saveFileByStream(inputStream, totalPath);
            if(result) startDeploy(sender, receiver, environment, readme);

            return ResultVoUtil.success();

        } catch (Exception e) {
            return ResultVoUtil.error(MessageEnum.E007, null, "未知异常:" + e.getMessage());
        }
    }

    /**
     * 开始发布流程
     * @param sender
     * @param receiver
     * @param environment
     * @param readme
     */
    public void startDeploy(String sender, String receiver, String environment, String readme) {

        //设置开始人
        identityService.setAuthenticatedUserId(sender);

        //设置指定负责人
        Map<String, Object> sendVar = new HashMap<>(4);
        sendVar.put("sender", sender);

        //创建进程
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance instance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINE_KEY, sendVar);

        //查询当前任务
        TaskService taskService = processEngine.getTaskService();
        Task currentTask = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();

        Map<String, Object> vars = new HashMap<>(4);
        vars.put("sender", sender);
        vars.put("checker", receiver);
        vars.put("environment", environment);
        vars.put("readme", readme);
        vars.put("reason", "需要紧急部署");

        //完成任务
        taskService.complete(currentTask.getId(), vars);
    }

    /**
     * 查找我的发布
     * @param request
     * @return
     */
    public Layui getMyDeploy(HttpServletRequest request) {

        String userName = request.getParameter("userName");

        List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().startedBy(userName).list();

        List<DeployEntity> vacList = new ArrayList<>();
        for (ProcessInstance instance : instanceList) {
            DeployEntity vac = getDeploy(instance);
            vacList.add(vac);
        }

        if(vacList.size() > 0) {
            PageUtils pageUtil = new PageUtils(vacList, vacList.size(), 10,1);
            return Layui.data(pageUtil.getTotalCount(), pageUtil.getList());
        }

        return Layui.data(0, null);
    }

    /**
     * 获取发布信息
     * @param instance
     * @return
     */
    private DeployEntity getDeploy(ProcessInstance instance) {

        String checker = runtimeService.getVariable(instance.getId(), "checker", String.class);
        String environment = runtimeService.getVariable(instance.getId(), "environment", String.class);
        String reason = runtimeService.getVariable(instance.getId(), "reason", String.class);
        String readme = runtimeService.getVariable(instance.getId(), "readme", String.class);

        DeployEntity vac = new DeployEntity();
        vac.setSender(instance.getStartUserId());
        vac.setReason(reason);

        Date startTime = instance.getStartTime(); // activiti 6 才有
        vac.setApplyTime(startTime);
        vac.setApplyStatus(instance.isEnded() ? "申请结束" : "等待审批");
        vac.setEnvironment(environment);
        vac.setReadme(CommonUtils.objectToStr(readme));
        vac.setChecker(checker);
        vac.setResult("");
        vac.setReceiveTime(DateUtils.getCurrentTime());
        return vac;
    }

    /**
     * 获取我的处理
     * @param request
     * @return
     */
    public Layui getMyAudit(HttpServletRequest request) {

        String userName = request.getParameter("userName");

        List<DeployTask> vacTaskList = getMyAudit(userName);

        if(vacTaskList.size() > 0) {
            PageUtils pageUtil = new PageUtils(vacTaskList, vacTaskList.size(), 10,1);
            return Layui.data(pageUtil.getTotalCount(), pageUtil.getList());
        }

        return Layui.data(0, null);
    }

    private List<DeployTask> getMyAudit(String userName) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取taskService
        TaskService taskService = processEngine.getTaskService();
        //3.根据流程key和任务负责人查询任务
        List<Task> taskList =  taskService.createTaskQuery()
                .processDefinitionKey(PROCESS_DEFINE_KEY)
                .taskAssignee(userName)
                .list();

        List<DeployTask> vacTaskList = new ArrayList<>();

        for (Task task : taskList) {
            DeployTask vacTask = new DeployTask();
            vacTask.setId(task.getId());
            vacTask.setName(task.getName());
            vacTask.setCreateTime(task.getCreateTime());
            String instanceId = task.getProcessInstanceId();
            ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();

            DeployEntity vac = getDeploy(instance);
            vacTask.setVac(vac);
            vacTaskList.add(vacTask);
        }

        return  vacTaskList;
    }

    public Object passAudit(String userName, DeployTask vacTask) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取taskService
        TaskService taskService = processEngine.getTaskService();

        String taskId = vacTask.getId();
        String result = vacTask.getVac().getResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("result", result);
        vars.put("auditor", userName);
        vars.put("auditTime", new Date());

        taskService.complete(taskId, vars);
        return true;
    }

    public Object myVacRecord(String userName) {
        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_DEFINE_KEY).startedBy(userName).finished()
                .orderByProcessInstanceEndTime().desc().list();

        List<DeployEntity> vacList = new ArrayList<>();
        for (HistoricProcessInstance hisInstance : hisProInstance) {
            DeployEntity vacation = new DeployEntity();
            vacation.setSender(hisInstance.getStartUserId());
            vacation.setApplyTime(hisInstance.getStartTime());
            vacation.setApplyStatus("申请结束");
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(hisInstance.getId()).list();
            CommonUtils.setVars(vacation, varInstanceList);
            vacList.add(vacation);
        }
        return vacList;
    }

    public Object myAuditRecord(String userName) {
        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_DEFINE_KEY).involvedUser(userName).finished()
                .orderByProcessInstanceEndTime().desc().list();

        List<String> auditTaskNameList = new ArrayList<>();
        auditTaskNameList.add("经理审批");
        auditTaskNameList.add("总监审批");
        List<DeployEntity> vacList = new ArrayList<>();
        for (HistoricProcessInstance hisInstance : hisProInstance) {
            List<HistoricTaskInstance> hisTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(hisInstance.getId()).processFinished()
                    .taskAssignee(userName)
                    .taskNameIn(auditTaskNameList)
                    .orderByHistoricTaskInstanceEndTime().desc().list();
            boolean isMyAudit = false;
            for (HistoricTaskInstance taskInstance : hisTaskInstanceList) {
                if (taskInstance.getAssignee().equals(userName)) {
                    isMyAudit = true;
                }
            }
            if (!isMyAudit) {
                continue;
            }
            DeployEntity vacation = new DeployEntity();
            vacation.setSender(hisInstance.getStartUserId());
            vacation.setApplyStatus("申请结束");
            vacation.setApplyTime(hisInstance.getStartTime());
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(hisInstance.getId()).list();
            CommonUtils.setVars(vacation, varInstanceList);
            vacList.add(vacation);
        }
        return vacList;
    }

}
