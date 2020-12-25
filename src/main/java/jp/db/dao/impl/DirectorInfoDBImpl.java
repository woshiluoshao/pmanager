package jp.db.dao.impl;

import jp.db.dao.IDirectorInfoDB;
import jp.db.jpa.IJPAImpl;
import jp.dto.LoginDto;
import jp.entity.DirectorInfoEntity;
import jp.entity.UserLoginLogEntity;
import jp.utils.CommonUtils;
import jp.utils.DateUtils;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DirectorInfoDBImpl implements IDirectorInfoDB {

    @Autowired
    IJPAImpl jpaDao;

    @Override
    public DirectorInfoEntity selectUserInfoById(String account) {

        DirectorInfoEntity model = null;

        try {
            String sql = "";
            sql += " select * from director_info where account = :account ";
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("account"   , CommonUtils.objectToStr(account));

            List<DirectorInfoEntity> modelList = jpaDao.queryListByParam(sql, DirectorInfoEntity.class, param);
            if(modelList.size() > 0) {
                 model = modelList.get(0);
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insertUserInfo(LoginDto loginDto) {

        int cnt = -1;
        try {
            String sql = "";
            sql += " insert into director_info(account, password, createTime) " +
                    "       values (:account  "   +
                    "              ,:password" +
                    "              ,:createTime)";

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("account"   , CommonUtils.objectToStr(loginDto.getAccount()));
            param.put("password"   , CommonUtils.objectToStr(loginDto.getPassword()));
            param.put("createTime"   , DateUtils.getCurrentTime());

            cnt = jpaDao.commonQueryByParam(sql, param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }


    public List<UserLoginLogEntity> selectLoginLog(Map<String, Object> param) {

        List<UserLoginLogEntity> logList = null;
        try {
            String sql = "";
            sql += " select *                  ";
            sql += "   from user_login_log     ";
            sql += "  where 1 = 1              ";

            if(!StringUtils.isEmpty(param.get("startTime"))) {
                sql +="and createTime >= :startTime ";
            }

            if(!StringUtils.isEmpty(param.get("endTime"))) {
                sql +="and createTime < :endTime ";
            }

            if(Integer.valueOf(param.get("action").toString()) > 0) {
                sql +="and action = :action";
            }

            sql += "   order by id, createTime ";

            int limit = (int)param.get("limit");
            int page = (int)param.get("page");

            if(limit >0) {
                sql += " limit  " + ((page -1) * limit) + "," + limit;
            }

            Map<String, Object> paramSql = new HashMap<String, Object>();
            if(!StringUtils.isEmpty(param.get("startTime"))) {
                paramSql.put("startTime"   , CommonUtils.objectToStr(param.get("startTime")));
            }
            if(!StringUtils.isEmpty(param.get("endTime"))) {
                paramSql.put("endTime"   , CommonUtils.objectToStr(param.get("endTime")));
            }

            if(Integer.valueOf(param.get("action").toString()) > 0) {
                paramSql.put("action"   , Integer.valueOf(param.get("action").toString()));
            }

            logList = jpaDao.queryListByParam(sql, UserLoginLogEntity.class, paramSql);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return logList;
    }

    public int countLoginLog(Map<String, Object> param) {

        String sql = "";

        sql += " select count(1)           ";
        sql += "   from user_login_log     ";
        sql += "  where 1 = 1              ";

        if(!StringUtils.isEmpty(param.get("startTime"))) {
            sql +="and createTime >= :startTime ";
        }

        if(!StringUtils.isEmpty(param.get("endTime"))) {
            sql +="and createTime < :endTime ";
        }

        if(Integer.valueOf(param.get("action").toString()) > 0) {
            sql +="and action = :action";
        }

        Map<String, Object> paramSql = new HashMap<String, Object>();
        if(!StringUtils.isEmpty(param.get("startTime"))) {
            paramSql.put("startTime"   , CommonUtils.objectToStr(param.get("startTime")));
        }
        if(!StringUtils.isEmpty(param.get("endTime"))) {
            paramSql.put("endTime"   , CommonUtils.objectToStr(param.get("endTime")));
        }

        if(Integer.valueOf(param.get("action").toString()) > 0) {
            paramSql.put("action"   , Integer.valueOf(param.get("action").toString()));
        }

        NativeQuery query = jpaDao.queryByParam(sql, param);

        BigInteger cnt = (BigInteger)query.getSingleResult();
        return cnt.intValue();
    }

    @Override
    public List<DirectorInfoEntity> selectUserList(String account) {

        String sql = "";
        sql += " select *                 ";
        sql += "   from director_info         ";
        sql += "  where account != :account " +
                " order by createTime     ";

        Map<String, Object> paramSql = new HashMap<String, Object>();
        paramSql.put("account"   , CommonUtils.objectToStr(account));
        List<DirectorInfoEntity> userList = jpaDao.queryListByParam(sql, DirectorInfoEntity.class, paramSql);

        return userList;
    }
}
