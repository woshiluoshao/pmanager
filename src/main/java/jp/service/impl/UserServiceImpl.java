package jp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jp.db.dao.ILoginDB;
import jp.db.redis.RedisUtil;
import jp.dto.LoginDto;
import jp.entity.UserListEntity;
import jp.enums.MessageEnum;
import jp.service.IUserService;
import jp.utils.ResultVoUtil;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

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

        redisUtil.set("userInfo", JSON.toJSONString(loginDto), 3000);

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
}
