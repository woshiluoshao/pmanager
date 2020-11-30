package jp.service.impl;

import com.alibaba.fastjson.JSON;
import jp.db.dao.ILoginDB;
import jp.db.redis.RedisUtil;
import jp.dto.LoginDto;
import jp.entity.UserListEntity;
import jp.entity.UserLoginLogEntity;
import jp.enums.MessageEnum;
import jp.service.IUserService;
import jp.utils.Layui;
import jp.utils.PageUtils;
import jp.utils.ResultVoUtil;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    RedisUtil redisUtil;

    @Autowired
    ILoginDB loginDB;

    @Override
    public ResultVo loginExe(LoginDto loginDto) {

        if(loginDto == null || StringUtils.isEmpty(loginDto.getUserId()) || StringUtils.isEmpty(loginDto.getPassword())) {
            System.out.println("参数不完整");
            return ResultVoUtil.error(MessageEnum.E002, null, "账号", "密码");
        }

        UserListEntity model = loginDB.selectUserInfoById(loginDto.getUserId());
        if(model == null)  return ResultVoUtil.error(MessageEnum.E011);

        redisUtil.set("userInfo", JSON.toJSON(loginDto), 1800);

        System.out.println("登录成功");
        return ResultVoUtil.success("登录成功");
    }

    @Override
    public ResultVo registerExe(LoginDto loginDto) {

        //判断库表是否存在;
        //存在则返回说账号已存在
        UserListEntity model = loginDB.selectUserInfoById(loginDto.getUserId());
        if(model != null)  return ResultVoUtil.error(MessageEnum.E014);

        //入库
        int cnt = loginDB.insertUserInfo(loginDto);
        if(cnt > 0)  return ResultVoUtil.success("注册成功");
        return ResultVoUtil.error(MessageEnum.E013);
    }

    public Layui selectLogAll(HttpServletRequest request) {
        try {

            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String action = request.getParameter("action");
            int limit = Integer.valueOf(request.getParameter("limit"));
            int page = Integer.valueOf(request.getParameter("page"));

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("startTime", startTime);
            param.put("endTime", endTime);
            param.put("action", action);
            param.put("limit", limit);
            param.put("page", page);

            //一般都是支持后端分页;减少数据的查询时间
            List<UserLoginLogEntity> list = loginDB.selectLoginLog(param);
            int total = loginDB.countLoginLog(param);
            if(list.size() > 0) {
                PageUtils pageUtil = new PageUtils(list, total, 10,1);
                return Layui.data(pageUtil.getTotalCount(), pageUtil.getList());
            }
        } catch (Exception e) {
        }

        return Layui.data(0, null);
    }

}
