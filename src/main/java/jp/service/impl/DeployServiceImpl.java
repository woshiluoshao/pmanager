package jp.service.impl;

import jp.entity.DeployEntity;
import jp.entity.DeployTask;
import jp.enums.MessageEnum;
import jp.service.IDeployService;
import jp.utils.*;
import jp.vo.ResultVo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;

    private static final String PROCESS_DEFINE_KEY = "deployProc";

    @Override
    public ResultVo addDeploy(HttpServletRequest request) {

        try {
            //获取参数
            String sender = request.getParameter("sender");
            String receiver = request.getParameter("receiver");
            String environment = request.getParameter("environment");
            String readme = request.getParameter("readme");

            if(StringUtils.isEmpty(CommonUtils.objectToStr(sender))) {
                return ResultVoUtil.error(MessageEnum.E001, null, "发送人");
            }
            if(StringUtils.isEmpty(CommonUtils.objectToStr(receiver))) {
                return ResultVoUtil.error(MessageEnum.E001, null, "接收人");
            }
            if(StringUtils.isEmpty(CommonUtils.objectToStr(environment))) {
                return ResultVoUtil.error(MessageEnum.E001, null, "发布环境");
            }
            if(StringUtils.isEmpty(CommonUtils.objectToStr(readme))) {
                return ResultVoUtil.error(MessageEnum.E001, null, "readme");
            }

            String fileSavePath = env.getProperty("manager.save-path");

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 获取上传的文件
            MultipartFile multiFile = multipartRequest.getFile("file");

            assert multiFile != null;
            String fileName = multiFile.getOriginalFilename();
            multiFile.getContentType();
            InputStream inputStream = multiFile.getInputStream();
            String totalPath = fileSavePath + fileName;
            FileUtils.createFolder(fileSavePath);
            boolean result = FileUtils.saveFileByStream(inputStream, totalPath);
            if(result) startDeploy(sender, receiver, environment, readme);

            return ResultVoUtil.success();

        } catch (Exception e) {
            return ResultVoUtil.error(MessageEnum.E007, null, "未知异常:" + e.getMessage());
        }
    }

    /**
     * 流程定义
     * @param sender
     */
    public void processDefinition(String sender) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name(sender + "的申请"+ DateUtils.getTimeStamp())
                .addClasspathResource("processes/deployProc.bpmn20.xml")
                .deploy();

        System.out.println("定义ID" + deployment.getId());
        System.out.println("定义名" + deployment.getName());
    }

    /**
     * 开始发布流程
     * @param sender
     * @param receiver
     * @param environment
     * @param readme
     */
    public void startDeploy(String sender, String receiver, String environment, String readme) {

        processDefinition(sender);

        //设置开始人
        identityService.setAuthenticatedUserId(sender);

        //设置指定负责人
        Map<String, Object> sendVar = new HashMap<>(4);
        sendVar.put("sender", sender);

        //创建进程
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //启动进程并将负责人作为参数传入
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
        vac.setProcessInstanceId(instance.getProcessInstanceId());
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

    /**
     * 处理任务
     * @param request
     * @return
     */
    @Override
    public ResultVo handleAudit(HttpServletRequest request) {

        try {
            String userName = request.getParameter("userName");
            String taskId = request.getParameter("taskId");
            String result = request.getParameter("result");


            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            //2.获取taskService
            TaskService taskService = processEngine.getTaskService();

            Map<String, Object> vars = new HashMap<>();
            vars.put("result", result);
            vars.put("checker", userName);
            vars.put("receiveTime", new Date());

            taskService.complete(taskId, vars);

            return ResultVoUtil.success();
        } catch (Exception e) {
            return ResultVoUtil.error("0001","审批异常");
        }
    }

    /**
     * 获取发送者发送的任务
     * @param request
     * @return
     */
    @Override
    public Layui getSendRecord(HttpServletRequest request) {

        String userName = request.getParameter("userName");

        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_DEFINE_KEY).startedBy(userName).finished()
                .orderByProcessInstanceEndTime().desc().list();

        List<DeployEntity> vacList = new ArrayList<>();

        for (HistoricProcessInstance hisInstance : hisProInstance) {
            DeployEntity vacation = new DeployEntity();
            vacation.setSender(hisInstance.getStartUserId());
            vacation.setApplyTime(hisInstance.getStartTime());
            String deleteReason = hisInstance.getDeleteReason();
            vacation.setApplyStatus(StringUtils.isEmpty(CommonUtils.objectToStr(deleteReason)) ? "审批结束" : "已撤回");
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(hisInstance.getId()).list();
            CommonUtils.setVars(vacation, varInstanceList);
            vacList.add(vacation);
        }

        if(vacList.size() > 0) {
            PageUtils pageUtil = new PageUtils(vacList, vacList.size(), 10,1);
            return Layui.data(pageUtil.getTotalCount(), pageUtil.getList());
        }

        return Layui.data(0, null);
    }

    /**
     * 获取自己处理的任务记录
     * @param request
     * @return
     */
    @Override
    public Layui getHandleRecord(HttpServletRequest request) {

        String userName = request.getParameter("userName");

        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_DEFINE_KEY).involvedUser(userName).finished()
                .orderByProcessInstanceEndTime().desc().list();

        List<DeployEntity> vacList = new ArrayList<>();

        String deleteReason = "";

        for (HistoricProcessInstance hisInstance : hisProInstance) {

            List<HistoricTaskInstance> hisTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(hisInstance.getId()).processFinished()
                    .taskAssignee(userName)
                    .orderByHistoricTaskInstanceEndTime().desc().list();
            boolean isMyAudit = false;
            for (HistoricTaskInstance taskInstance : hisTaskInstanceList) {

                //此处写法是过滤发送者撤回的记录
                deleteReason = taskInstance.getDeleteReason();
                if(!StringUtils.isEmpty(deleteReason)) continue;
                //此处写法是过滤发送者撤回的记录

                //taskInstance.getName().equals("receiveTask")是为了精确查找到审批的人，实际业务肯定有修改
                if (taskInstance.getAssignee().equals(userName) && taskInstance.getName().equals("receiveTask")) {
                    isMyAudit = true;
                }
            }
            if (!isMyAudit) {
                continue;
            }

            String msg = StringUtils.isEmpty(CommonUtils.objectToStr(deleteReason)) ? "已审核通过" : "已撤回";

            DeployEntity vacation = new DeployEntity();
            vacation.setSender(hisInstance.getStartUserId());
            vacation.setApplyStatus(msg);
            vacation.setApplyTime(hisInstance.getStartTime());
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(hisInstance.getId()).list();
            CommonUtils.setVars(vacation, varInstanceList);
            vacList.add(vacation);
        }

        if(vacList.size() > 0) {
            PageUtils pageUtil = new PageUtils(vacList, vacList.size(), 10,1);
            return Layui.data(pageUtil.getTotalCount(), pageUtil.getList());
        }

        return Layui.data(0, null);
    }

    /**
     * 撤回任务
     * @param request
     * @return
     */
    @Override
    public ResultVo recallDeploy(HttpServletRequest request) {

        String userName = request.getParameter("userName");
        String processInstanceId = request.getParameter("processInstanceId");
        String taskId = request.getParameter("taskId");

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if(task==null) {
            throw new ServiceException("流程未启动或已执行完成，无法撤回");
        }

        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .asc()
                .list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        for(HistoricTaskInstance hti : htiList) {
            if(userName.equals(hti.getAssignee())) {
                myTaskId = hti.getId();
                myTask = hti;
                break;
            }
        }
        if(null==myTaskId) {
            throw new ServiceException("该任务非当前用户提交，无法撤回");
        }

        String processDefinitionId = myTask.getProcessDefinitionId();
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        //变量
//		Map<String, VariableInstance> variables = runtimeService.getVariableInstances(currentTask.getExecutionId());
        String myActivityId = null;
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                .executionId(myTask.getExecutionId()).finished().list();
        for(HistoricActivityInstance hai : haiList) {
            if(myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);


        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

        //清理活动方向
        flowNode.getOutgoingFlows().clear();

        runtimeService.deleteProcessInstance(processInstanceId, "写错了，删除下");
//        //建立新方向
//        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
//        SequenceFlow newSequenceFlow = new SequenceFlow();
//        newSequenceFlow.setId("newSequenceFlowId");
//        newSequenceFlow.setSourceFlowElement(flowNode);
//        newSequenceFlow.setTargetFlowElement(myFlowNode);
//        newSequenceFlowList.add(newSequenceFlow);
//        flowNode.setOutgoingFlows(newSequenceFlowList);
//
//        Authentication.setAuthenticatedUserId(userName);
//        taskService.addComment(task.getId(), task.getProcessInstanceId(), "撤回");
//
//        Map<String,Object> currentVariables = new HashMap<String,Object>();
//        currentVariables.put("sender", userName);
//        currentVariables.put("checker", "mumu");
//        //完成任务
//        taskService.complete(task.getId(),currentVariables);
        //taskService.deleteTask(task.getId());
        //恢复原方向
        //flowNode.setOutgoingFlows(oriSequenceFlows);
        return ResultVoUtil.success();
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
}
