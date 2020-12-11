package jp.db.dao;

import jp.dto.LoginDto;
import jp.entity.DirectorInfoEntity;
import jp.entity.UserLoginLogEntity;

import java.util.List;
import java.util.Map;

public interface IDirectorInfoDB {

    DirectorInfoEntity selectUserInfoById(String userId);
    int insertUserInfo(LoginDto loginDto);
    List<UserLoginLogEntity> selectLoginLog(Map<String, Object> param);
    int countLoginLog(Map<String, Object> param);
    List<DirectorInfoEntity> selectUserList(String userId);
}
