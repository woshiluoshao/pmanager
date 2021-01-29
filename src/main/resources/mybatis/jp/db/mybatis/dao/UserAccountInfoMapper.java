package jp.db.mybatis.dao;

import jp.db.mybatis.model.UserAccountInfo;

public interface UserAccountInfoMapper {
    int deleteByPrimaryKey(String account);

    int insert(UserAccountInfo record);

    int insertSelective(UserAccountInfo record);

    UserAccountInfo selectByPrimaryKey(String account);

    int updateByPrimaryKeySelective(UserAccountInfo record);

    int updateByPrimaryKeyWithBLOBs(UserAccountInfo record);

    int updateByPrimaryKey(UserAccountInfo record);
}