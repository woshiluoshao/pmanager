package jp.service.impl;

import jp.db.dao.IInterfaceResponseInfoDB;
import jp.entity.InterfaceResponseInfoEntity;
import jp.enums.MessageEnum;
import jp.service.IInterfaceService;
import jp.utils.*;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterfaceServiceImpl implements IInterfaceService {

    @Autowired
    IInterfaceResponseInfoDB interfaceResponseInfoDB;

    @Override
    public ResultVo addPerResInterface(HttpServletRequest request) {

        InterfaceResponseInfoEntity responseInfoEntity = new InterfaceResponseInfoEntity();
        String director = request.getParameter("director");
        String project = request.getParameter("project");

        responseInfoEntity.setProjectId(CommonUtils.getUuid());
        responseInfoEntity.setDirector(director);
        responseInfoEntity.setProject(project);
        responseInfoEntity.setProjectName(request.getParameter("projectName"));
        responseInfoEntity.setDevelopmentLanguage(request.getParameter("developmentLanguage"));
        responseInfoEntity.setDevelopmentArchitect(request.getParameter("developmentArchitect"));
        responseInfoEntity.setFunctionPoint(request.getParameter("functionPoint"));
        responseInfoEntity.setComments(request.getParameter("comments"));
        Date nowTime = DateUtils.getCurrentTime();
        responseInfoEntity.setCreateTime(nowTime);
        responseInfoEntity.setUpdateTime(nowTime);
        responseInfoEntity.setUpdateCount(0);
        responseInfoEntity.setProjectStart(request.getParameter("projectStart"));
        responseInfoEntity.setProjectEnd(request.getParameter("projectEnd"));
        responseInfoEntity.setCollaborator(request.getParameter("collaborator"));
        responseInfoEntity.setProjectManager(request.getParameter("projectManager"));
        responseInfoEntity.setProjectStatus(request.getParameter("projectStatus"));

        List<InterfaceResponseInfoEntity> keyModel = interfaceResponseInfoDB.getPersonResByKey(director, project);
        if(keyModel!= null && keyModel.size() > 0) {

            return ResultVoUtil.error(MessageEnum.W003, null, "项目(project)");
        }

        int cnt = interfaceResponseInfoDB.addPersonRes(responseInfoEntity);

        if(cnt > 0) return ResultVoUtil.success("添加成功");

        return ResultVoUtil.error(MessageEnum.E007, null, "未知异常");
    }

    @Override
    public Layui selectPerResInterface(HttpServletRequest request) {

        try {

            int limit = Integer.parseInt(request.getParameter("limit"));
            int page = Integer.parseInt(request.getParameter("page"));

            Map<String, Object> param = new HashMap<>();
            param.put("limit", limit);
            param.put("page", page);

            //一般都是支持后端分页;减少数据的查询时间
            List<InterfaceResponseInfoEntity> list = interfaceResponseInfoDB.getPersonResList(param);
            int total = interfaceResponseInfoDB.countPerResList(param);
            if(list.size() > 0) {
                PageUtils pageUtil = new PageUtils(list, total, 10,1);
                return Layui.data(pageUtil.getTotalCount(), pageUtil.getList());
            }
        } catch (Exception ignored) {
        }

        return Layui.data(0, null);
    }

    @Override
    public ResultVo delPerResInterface(HttpServletRequest request) {

        String projectIdList = request.getParameter("projectId");

        String[] array = projectIdList.split(",");

        if(array.length <= 0 ) return ResultVoUtil.error(MessageEnum.E007, null, "工程不存在");

        StringBuilder suffer= new StringBuilder();
        for (String s : array) {
            suffer.append("\'");
            suffer.append(s);
            suffer.append("\',");
        }

        String projectIdParam = suffer.substring(0, suffer.length()-1);

        int cnt = interfaceResponseInfoDB.delBatchPersonRes(projectIdParam);

        if(cnt > 0) return ResultVoUtil.success();

        return ResultVoUtil.error(MessageEnum.E015);
    }
}
