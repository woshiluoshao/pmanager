package jp.db.dao.impl;

import jp.db.dao.ILoginDB;
import jp.db.jpa.IJPAImpl;
import jp.dto.LoginDto;
import jp.entity.UserListEntity;
import jp.utils.CommonUtils;
import jp.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LoginDBImpl implements ILoginDB {

    @Autowired
    IJPAImpl jpaDao;

    @Override
    public UserListEntity selectUserInfoById(String userId) {

        UserListEntity model = null;

        try {
            String sql = "";
            sql += " select * from user_list where userId = :userId ";
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId"   , CommonUtils.objectToStr(userId));

            List<UserListEntity> modelList = jpaDao.queryListByParam(sql, UserListEntity.class, param);
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
            sql += " insert into user_list(userId, password, createTime) " +
                    "       values (:userId  "   +
                    "              ,:password" +
                    "              ,:createTime)";

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId"   , CommonUtils.objectToStr(loginDto .getUserId()));
            param.put("password"   , CommonUtils.objectToStr(loginDto .getPassword()));
            param.put("createTime"   , DateUtils.getCurrentTime());

            cnt = jpaDao.commonQueryByParam(sql, param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }
}
