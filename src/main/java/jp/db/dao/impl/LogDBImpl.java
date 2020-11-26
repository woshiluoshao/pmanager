package jp.db.dao.impl;

import jp.db.dao.ILogDB;
import jp.db.jpa.IJPAImpl;
import jp.entity.UserOperationLogEntity;
import jp.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class LogDBImpl implements ILogDB {

    @Autowired
    IJPAImpl jpaDao;

    @Override
    public void insertUserOperaLog(UserOperationLogEntity logEntity) {

        try {
            String sql = "";
            sql += " insert into user_operation_log ( ";
            //sql += "        id                        ";
            sql += "        userId                   ";
            sql += "        ,username                 ";
            sql += "        ,module                   ";
            sql += "        ,methods                  ";
            sql += "        ,methodName               ";
            sql += "        ,acceptData               ";
            sql += "        ,returnData               ";
            sql += "        ,actionUrl                ";
            sql += "        ,serverIp                 ";
            sql += "        ,visitIp                  ";
            sql += "        ,createTime               ";
            sql += "        ,comments )                ";
            sql += "  values (                        ";
            //sql += "         :id                      ";
            sql += "        :userId                  ";
            sql += "        ,:username                ";
            sql += "        ,:module                  ";
            sql += "        ,:methods                 ";
            sql += "        ,:methodName              ";
            sql += "        ,:acceptData              ";
            sql += "        ,:returnData              ";
            sql += "        ,:actionUrl               ";
            sql += "        ,:serverIp                ";
            sql += "        ,:visitIp                 ";
            sql += "        ,:createTime              ";
            sql += "        ,:comments )               ";

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId"   , CommonUtils.objectToStr(logEntity.getUserId()));
            param.put("username" , CommonUtils.objectToStr(logEntity.getUsername()));
            param.put("module"  , CommonUtils.objectToStr(logEntity.getModule()));
            param.put("methods"    , CommonUtils.objectToStr(logEntity.getMethods()));
            param.put("methodName"    , CommonUtils.objectToStr(logEntity.getMethodName()));
            param.put("acceptData"    , CommonUtils.objectToStr(logEntity.getAcceptData()));
            param.put("returnData"      , CommonUtils.objectToStr(logEntity.getReturnData()));
            param.put("actionUrl"       , CommonUtils.objectToStr(logEntity.getActionUrl()));
            param.put("serverIp" , CommonUtils.objectToStr(logEntity.getServerIp()) );
            param.put("visitIp" , CommonUtils.objectToStr(logEntity.getVisitIp()));
            param.put("createTime" , logEntity.getCreateTime());
            param.put("comments" , logEntity.getComments());
            int cnt = jpaDao.commonQueryByParam(sql, param);
            System.out.println("用户操作日志数:" + cnt);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
