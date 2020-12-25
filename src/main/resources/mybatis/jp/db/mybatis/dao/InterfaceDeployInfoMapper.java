package jp.db.mybatis.dao;

import jp.db.mybatis.model.InterfaceDeployInfo;

public interface InterfaceDeployInfoMapper {
    int deleteByPrimaryKey(Integer uuid);

    int insert(InterfaceDeployInfo record);

    int insertSelective(InterfaceDeployInfo record);

    InterfaceDeployInfo selectByPrimaryKey(Integer uuid);

    int updateByPrimaryKeySelective(InterfaceDeployInfo record);

    int updateByPrimaryKeyWithBLOBs(InterfaceDeployInfo record);

    int updateByPrimaryKey(InterfaceDeployInfo record);
}