package jp.service;

import jp.db.dao.ILogDB;
import jp.entity.UserOperationLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ThreadService {

    @Autowired
    ILogDB daoImpl;

    @Async(value = "multiThread")
    public void runThread(UserOperationLogEntity logEntity) {

        daoImpl.insertUserOperaLog(logEntity);
    }
}
