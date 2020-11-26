package jp.db.dao;

import jp.dto.LoginDto;
import jp.entity.UserListEntity;

public interface ILoginDB {

    UserListEntity selectUserInfoById(String userId);
    int insertUserInfo(LoginDto loginDto);
}
