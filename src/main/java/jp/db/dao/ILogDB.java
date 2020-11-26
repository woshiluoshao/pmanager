package jp.db.dao;

import jp.entity.UserOperationLogEntity;

public interface ILogDB {

    void insertUserOperaLog(UserOperationLogEntity logEntity);
}
