package jp.service.impl;

import jp.db.dao.IPersonResInterDB;
import jp.entity.PersonResInterfaceEntity;
import jp.entity.UserLoginLogEntity;
import jp.enums.MessageEnum;
import jp.service.IInterfaceService;
import jp.utils.DateUtils;
import jp.utils.Layui;
import jp.utils.PageUtils;
import jp.utils.ResultVoUtil;
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
    IPersonResInterDB personResInterDB;

    @Override
    public ResultVo addPerResInterface(HttpServletRequest request) {

        PersonResInterfaceEntity personResInterfaceEntity = new PersonResInterfaceEntity();
        personResInterfaceEntity.setDirector(request.getParameter("director"));
        personResInterfaceEntity.setProject(request.getParameter("project"));
        personResInterfaceEntity.setProjectName(request.getParameter("projectName"));
        personResInterfaceEntity.setDeployType(request.getParameter("deployType"));
        personResInterfaceEntity.setDevelopmentTool(request.getParameter("developmentTool"));
        personResInterfaceEntity.setDevelopmentArchitect(request.getParameter("developmentArchitect"));
        personResInterfaceEntity.setFunctionPoint(request.getParameter("functionPoint"));
        personResInterfaceEntity.setComments(request.getParameter("comments"));
        Date nowTime = DateUtils.getCurrentTime();
        personResInterfaceEntity.setCreateTime(nowTime);
        personResInterfaceEntity.setUpdateTime(nowTime);

        int cnt = personResInterDB.addPersonRes(personResInterfaceEntity);

        if(cnt > 0) return ResultVoUtil.success("添加成功");

        return ResultVoUtil.error(MessageEnum.E007, null, "未知异常");
    }

    @Override
    public Layui selectPerResInterface(HttpServletRequest request) {

        try {

            int limit = Integer.valueOf(request.getParameter("limit"));
            int page = Integer.valueOf(request.getParameter("page"));

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("limit", limit);
            param.put("page", page);

            //一般都是支持后端分页;减少数据的查询时间
            List<PersonResInterfaceEntity> list = personResInterDB.getPersonResList(param);
            int total = personResInterDB.countPerResList(param);
            if(list.size() > 0) {
                PageUtils pageUtil = new PageUtils(list, total, 10,1);
                return Layui.data(pageUtil.getTotalCount(), pageUtil.getList());
            }
        } catch (Exception e) {
        }

        return Layui.data(0, null);
    }
}
