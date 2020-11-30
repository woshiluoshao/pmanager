package jp.db.dao;

import jp.dto.LoginDto;
import jp.entity.UserListEntity;
import jp.entity.UserLoginLogEntity;

import java.util.List;
import java.util.Map;

public interface ILoginDB {

    UserListEntity selectUserInfoById(String userId);
    int insertUserInfo(LoginDto loginDto);
    List<UserLoginLogEntity> selectLoginLog(Map<String, Object> param);
    int countLoginLog(Map<String, Object> param);
}
