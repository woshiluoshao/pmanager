package jp.db.dao.impl;

import jp.db.dao.IDynamicParamDB;
import jp.db.jpa.IJPAImpl;
import jp.entity.DynamicParamEntity;
import jp.utils.CommonUtils;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DynamicParamDBImpl implements IDynamicParamDB {

    @Autowired
    IJPAImpl jpaDao;

    @Override
    public List<DynamicParamEntity> getDynamicParamList(Map<String, Object> param) {

        List<DynamicParamEntity> paramList = null;
        try {
            String sql = "";
            sql += " select *                         ";
            sql += "   from dynamic_param             ";
            sql += "  where paramKey = :paramKey      ";
            sql += "   order by paramKey              ";

            if(param.get("limit") != null && param.get("page") != null) {
                int limit = (int) param.get("limit");
                int page = (int) param.get("page");

                if (limit > 0) {
                    sql += " limit  " + ((page - 1) * limit) + "," + limit;
                }
            }

            Map<String, Object> paramSql = new HashMap<String, Object>();
            paramSql.put("paramKey", CommonUtils.objectToStr(param.get("paramKey")));

            paramList = jpaDao.queryListByParam(sql, DynamicParamEntity.class, paramSql);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paramList;
    }

    @Override
    public int countDynamicParamList(Map<String, Object> param) {

        String sql = "";

        sql += " select count(1)            ";
        sql += "   from dynamic_param       ";
        sql += "  where paramKey = :paramKey";

        Map<String, Object> paramSql = new HashMap<String, Object>();
        paramSql.put("paramKey", CommonUtils.objectToStr(param.get("paramKey")));

        NativeQuery query = jpaDao.queryByParam(sql, param);

        BigInteger cnt = (BigInteger)query.getSingleResult();
        return cnt.intValue();
    }

    @Override
    public int delDynamicParamById(String id) {

        int cnt = -1;
        try {
            String sql = "";

            sql += " delete                 ";
            sql += "   from dynamic_param   ";
            sql += "  where id = :id        ";

            Map<String, Object> paramSql = new HashMap<String, Object>();
            paramSql.put("id", CommonUtils.objectToStr(id));

            cnt = jpaDao.commonQueryByParam(sql, paramSql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt;
    }
}
