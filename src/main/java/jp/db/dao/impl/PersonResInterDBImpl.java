package jp.db.dao.impl;

import jp.db.dao.IPersonResInterDB;
import jp.db.jpa.IJPAImpl;
import jp.entity.PersonResInterfaceEntity;
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
public class PersonResInterDBImpl implements IPersonResInterDB {

    @Autowired
    IJPAImpl jpaDao;

    @Override
    public List<PersonResInterfaceEntity> getPersonResList(Map<String, Object> param) {

        List<PersonResInterfaceEntity> logList = null;
        try {
            String sql = "";
            sql += " select *                         ";
            sql += "   from person_response_interface ";
            sql += "  where 1 = 1                     ";
            sql += "   order by createTime desc       ";

            int limit = (int)param.get("limit");
            int page = (int)param.get("page");

            if(limit >0) {
                sql += " limit  " + ((page -1) * limit) + "," + limit;
            }

            Map<String, Object> paramSql = new HashMap<String, Object>();


            logList = jpaDao.queryListByParam(sql, PersonResInterfaceEntity.class, paramSql);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return logList;
    }

    @Override
    public int countPerResList(Map<String, Object> param) {

        String sql = "";

        sql += " select count(1)                    ";
        sql += "   from person_response_interface   ";

        Map<String, Object> paramSql = new HashMap<String, Object>();

        NativeQuery query = jpaDao.queryByParam(sql, param);

        BigInteger cnt = (BigInteger)query.getSingleResult();
        return cnt.intValue();
    }

    @Override
    public int delPersonRes(String userId, String project) {

        String sql = "";

        sql += " delete                             ";
        sql += "   from person_response_interface   ";
        sql += "  where userId  = :userId           " +
                "   and project = :project          ";

        Map<String, Object> paramSql = new HashMap<String, Object>();
        paramSql.put("userId"   , CommonUtils.objectToStr(userId));
        paramSql.put("project"  , CommonUtils.objectToStr(project));

        NativeQuery query = jpaDao.queryByParam(sql, paramSql);

        BigInteger cnt = (BigInteger)query.getSingleResult();
        return cnt.intValue();
    }

    @Override
    public int updatePersonRes(PersonResInterfaceEntity personResInterfaceEntity) {

        String sql = "";

        sql += " insert into person_response_interface ( ";
        sql += "        userId                           ";
        sql += "       ,project                          ";
        sql += "       ,projectName                      ";
        sql += "       ,deployType                       ";
        sql += "       ,developmentTool                  ";
        sql += "       ,developmentArchi                 ";
        sql += "       ,functionPoint                    ";
        sql += "       ,comments                         ";
        sql += "       ,createTime                       ";
        sql += "       ,updateCount                      ";
        sql += "       ,updateTime ) values (            ";
        sql += "        :userId                          ";
        sql += "       ,:project                         ";
        sql += "       ,:projectName                     ";
        sql += "       ,:deployType                      ";
        sql += "       ,:developmentTool                 ";
        sql += "       ,:developmentArchi                ";
        sql += "       ,:functionPoint                   ";
        sql += "       ,:comments                        ";
        sql += "       ,:createTime                      ";
        sql += "       ,:updateCount                     ";
        sql += "       ,:updateTime 	    )            ";

        Map<String, Object> paramSql = new HashMap<String, Object>();
        //paramSql.put("userId"   , CommonUtils.objectToStr(userId));
        //paramSql.put("action"   , CommonUtils.objectToStr(project));

        return 0;
    }

    @Override
    public int addPersonRes(PersonResInterfaceEntity personResInterfaceEntity) {

        int addCnt = -1;
        try {
            String sql = "";

            sql += " insert into person_response_interface ( ";
            sql += "        director                         ";
            sql += "       ,project                          ";
            sql += "       ,projectName                      ";
            sql += "       ,deployType                       ";
            sql += "       ,developmentTool                  ";
            sql += "       ,developmentArchitect             ";
            sql += "       ,functionPoint                    ";
            sql += "       ,comments                         ";
            sql += "       ,createTime                       ";
            sql += "       ,updateCount                      ";
            sql += "       ,updateTime ) values (            ";
            sql += "        :director                        ";
            sql += "       ,:project                         ";
            sql += "       ,:projectName                     ";
            sql += "       ,:deployType                      ";
            sql += "       ,:developmentTool                 ";
            sql += "       ,:developmentArchitect            ";
            sql += "       ,:functionPoint                   ";
            sql += "       ,:comments                        ";
            sql += "       ,:createTime                      ";
            sql += "       ,:updateCount                     ";
            sql += "       ,:updateTime 	    )            ";

            Map<String, Object> paramSql = new HashMap<String, Object>();
            paramSql.put("director"   , CommonUtils.objectToStr(personResInterfaceEntity.getDirector()));
            paramSql.put("project"   , CommonUtils.objectToStr(personResInterfaceEntity.getProject()));
            paramSql.put("projectName"   , CommonUtils.objectToStr(personResInterfaceEntity.getProjectName()));
            paramSql.put("deployType"   , CommonUtils.objectToStr(personResInterfaceEntity.getDeployType()));
            paramSql.put("developmentTool"   , CommonUtils.objectToStr(personResInterfaceEntity.getDevelopmentTool()));
            paramSql.put("developmentArchitect"   , CommonUtils.objectToStr(personResInterfaceEntity.getDevelopmentArchitect()));
            paramSql.put("functionPoint"   , CommonUtils.objectToStr(personResInterfaceEntity.getFunctionPoint()));
            paramSql.put("comments"   , CommonUtils.objectToStr(personResInterfaceEntity.getComments()));
            paramSql.put("createTime"   , CommonUtils.objectToStr(personResInterfaceEntity.getCreateTime()));
            paramSql.put("updateCount"   , 0);
            paramSql.put("updateTime"   , CommonUtils.objectToStr(personResInterfaceEntity.getCreateTime()));

            addCnt = jpaDao.commonQueryByParam(sql, paramSql);

            //BigInteger cnt = (BigInteger)query.getSingleResult();
            //addCnt = cnt.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addCnt;
    }
}
