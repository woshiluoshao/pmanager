package jp.db.dao.impl;

import jp.db.dao.IInterfaceResponseInfoDB;
import jp.db.jpa.IJPAImpl;
import jp.entity.InterfaceResponseInfoEntity;
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
public class InterfaceResponseInfoDBImpl implements IInterfaceResponseInfoDB {

    @Autowired
    IJPAImpl jpaDao;

    @Override
    public List<InterfaceResponseInfoEntity> getPersonResList(Map<String, Object> param) {

        List<InterfaceResponseInfoEntity> logList = null;
        try {
            String sql = "";
            sql += " select *                         ";
            sql += "   from interface_response_info   ";
            sql += "  where 1 = 1                     ";
            sql += "   order by createTime desc       ";

            int limit = (int)param.get("limit");
            int page = (int)param.get("page");

            if(limit >0) {
                sql += " limit  " + ((page -1) * limit) + "," + limit;
            }

            Map<String, Object> paramSql = new HashMap<String, Object>();


            logList = jpaDao.queryListByParam(sql, InterfaceResponseInfoEntity.class, paramSql);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return logList;
    }

    @Override
    public int countPerResList(Map<String, Object> param) {

        String sql = "";

        sql += " select count(1)                    ";
        sql += "   from interface_response_info   ";

        Map<String, Object> paramSql = new HashMap<String, Object>();

        NativeQuery query = jpaDao.queryByParam(sql, param);

        BigInteger cnt = (BigInteger)query.getSingleResult();
        return cnt.intValue();
    }

    @Override
    public int delPersonRes(String projectId) {

        int cnt = -1;
        try {
            String sql = "";

            sql += " delete                           ";
            sql += "   from interface_response_info   ";
            sql += "  where projectId = :projectId    ";

            Map<String, Object> paramSql = new HashMap<String, Object>();
            paramSql.put("projectId"   , CommonUtils.objectToStr(projectId));

            cnt = jpaDao.commonQueryByParam(sql, paramSql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }

    @Override
    public int delBatchPersonRes(String projectIdList) {

        int cnt = -1;
        try {
            String sql = "";

            sql += " delete                                    ";
            sql += "   from interface_response_info            ";
            sql += "  where projectId in ("+ projectIdList +") ";

            Map<String, Object> paramSql = new HashMap<String, Object>();

            cnt = jpaDao.commonQueryByParam(sql, paramSql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }

    @Override
    public int updatePersonRes(InterfaceResponseInfoEntity responseInfoEntity) {

        int cnt = -1;
        try {
            String sql = "";

            sql += "  update interface_response_info ";
            sql += "     set project        = :project                     ";
            sql += "       , projectName    = :projectName                 ";
            sql += "       , projectStart   = :projectStart                ";
            if(!StringUtils.isEmpty(responseInfoEntity.getProjectEnd())) {
                sql += "       , projectEnd     = :projectEnd                  ";
            }
            sql += "       , director       = :director                    ";
            if(!StringUtils.isEmpty(responseInfoEntity.getCollaborator())) {
                sql += "       , collaborator   = :collaborator                ";
            }
            if(!StringUtils.isEmpty(responseInfoEntity.getProjectManager())) {
                sql += "       , projectManager = :projectManager              ";
            }
            sql += "       , projectStatus  = :projectStatus               ";
            sql += "       , developmentLanguage  = :developmentLanguage   ";
            sql += "       , developmentArchitect = :developmentArchitect  ";
            sql += "       , functionPoint        = :functionPoint         ";
            if(!StringUtils.isEmpty(responseInfoEntity.getComments())) {
                sql += "       , comments             = :comments              ";
            }
            sql += "       , updateCount          = :updateCount           ";
            sql += "       , updateTime           = :updateTime            ";
            sql += "  where projectId             = :projectId             ";

            Map<String, Object> paramSql = new HashMap<String, Object>();
            paramSql.put("projectId"   , CommonUtils.objectToStr(responseInfoEntity.getProjectId()));
            paramSql.put("project"   , CommonUtils.objectToStr(responseInfoEntity.getProject()));
            paramSql.put("projectName"   , CommonUtils.objectToStr(responseInfoEntity.getProjectName()));
            paramSql.put("projectStart"   , CommonUtils.objectToStr(responseInfoEntity.getProjectStart()));
            paramSql.put("projectEnd"   , CommonUtils.objectToStr(responseInfoEntity.getProjectEnd()));
            paramSql.put("director"   , CommonUtils.objectToStr(responseInfoEntity.getDirector()));
            paramSql.put("collaborator"   , CommonUtils.objectToStr(responseInfoEntity.getCollaborator()));
            paramSql.put("projectManager"   , CommonUtils.objectToStr(responseInfoEntity.getProjectManager()));
            paramSql.put("projectStatus"   , CommonUtils.objectToStr(responseInfoEntity.getProjectStatus()));
            paramSql.put("developmentLanguage"   , CommonUtils.objectToStr(responseInfoEntity.getDevelopmentLanguage()));
            paramSql.put("developmentArchitect"   , CommonUtils.objectToStr(responseInfoEntity.getDevelopmentArchitect()));
            paramSql.put("functionPoint"   , CommonUtils.objectToStr(responseInfoEntity.getFunctionPoint()));
            paramSql.put("comments"   , CommonUtils.objectToStr(responseInfoEntity.getComments()));
            paramSql.put("updateCount"   , responseInfoEntity.getUpdateCount() + 1);
            paramSql.put("updateTime"   , DateUtils.getCurrentTime());

            cnt = jpaDao.commonQueryByParam(sql, paramSql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }

    @Override
    public int addPersonRes(InterfaceResponseInfoEntity personResInterfaceEntity) {

        int addCnt = -1;
        try {
            String sql = "";

            sql += " insert into interface_response_info ( ";
            sql += "        projectId                      ";
            sql += "       ,director                       ";
            sql += "       ,project                        ";
            sql += "       ,projectName                    ";
            sql += "       ,developmentLanguage            ";
            sql += "       ,developmentArchitect           ";
            sql += "       ,functionPoint                  ";
            sql += "       ,comments                       ";
            sql += "       ,createTime                     ";
            sql += "       ,updateCount                    ";
            sql += "       ,updateTime                     ";
            sql += "       ,projectStart                   ";
            sql += "       ,projectEnd                     ";
            sql += "       ,collaborator                   ";
            sql += "       ,projectManager                 ";
            sql += "       ,projectStatus                  ";
            sql += "       ,flowChart ) values (           ";
            sql += "        :projectId                     ";
            sql += "       ,:director                      ";
            sql += "       ,:project                       ";
            sql += "       ,:projectName                   ";
            sql += "       ,:developmentLanguage           ";
            sql += "       ,:developmentArchitect          ";
            sql += "       ,:functionPoint                 ";
            sql += "       ,:comments                      ";
            sql += "       ,:createTime                    ";
            sql += "       ,:updateCount                   ";
            sql += "       ,:updateTime 	               ";
            sql += "       ,:projectStart                  ";
            sql += "       ,:projectEnd                    ";
            sql += "       ,:collaborator                  ";
            sql += "       ,:projectManager                ";
            sql += "       ,:projectStatus                 ";
            sql += "       ,:flowChart )                   ";

            Map<String, Object> paramSql = new HashMap<String, Object>();
            paramSql.put("projectId"   , CommonUtils.objectToStr(personResInterfaceEntity.getProjectId()));
            paramSql.put("director"   , CommonUtils.objectToStr(personResInterfaceEntity.getDirector()));
            paramSql.put("project"   , CommonUtils.objectToStr(personResInterfaceEntity.getProject()));
            paramSql.put("projectName"   , CommonUtils.objectToStr(personResInterfaceEntity.getProjectName()));
            paramSql.put("developmentLanguage"   , CommonUtils.objectToStr(personResInterfaceEntity.getDevelopmentLanguage()));
            paramSql.put("developmentArchitect"   , CommonUtils.objectToStr(personResInterfaceEntity.getDevelopmentArchitect()));
            paramSql.put("functionPoint"   , CommonUtils.objectToStr(personResInterfaceEntity.getFunctionPoint()));
            paramSql.put("comments"   , CommonUtils.objectToStr(personResInterfaceEntity.getComments()));
            paramSql.put("createTime"   , CommonUtils.objectToStr(personResInterfaceEntity.getCreateTime()));
            paramSql.put("updateCount"   , 0);
            paramSql.put("updateTime"   , CommonUtils.objectToStr(personResInterfaceEntity.getCreateTime()));
            paramSql.put("projectStart"   , CommonUtils.objectToStr(personResInterfaceEntity.getProjectStart()));
            paramSql.put("projectEnd"   , CommonUtils.objectToStr(personResInterfaceEntity.getProjectEnd()));
            paramSql.put("collaborator"   , CommonUtils.objectToStr(personResInterfaceEntity.getCollaborator()));
            paramSql.put("projectManager"   , CommonUtils.objectToStr(personResInterfaceEntity.getProjectManager()));
            paramSql.put("projectStatus"   , CommonUtils.objectToStr(personResInterfaceEntity.getProjectStatus()));
            paramSql.put("flowChart"   , CommonUtils.objectToStr(personResInterfaceEntity.getFlowChart()));

            addCnt = jpaDao.commonQueryByParam(sql, paramSql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addCnt;
    }

    @Override
    public List<InterfaceResponseInfoEntity> getPersonResByKey(String director, String project) {

        List<InterfaceResponseInfoEntity> logList = null;
        try {
            String sql = "";
            sql += " select *                         ";
            sql += "   from interface_response_info   ";
            sql += "  where director = :director      ";
            sql += "    and project  = :project       ";

            Map<String, Object> paramSql = new HashMap<String, Object>();
            paramSql.put("director"   , CommonUtils.objectToStr(director));
            paramSql.put("project"   , CommonUtils.objectToStr(project));
            logList = jpaDao.queryListByParam(sql, InterfaceResponseInfoEntity.class, paramSql);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logList;
    }

    @Override
    public List<InterfaceResponseInfoEntity> getResponseInfoByProjectId(String projectId) {

        List<InterfaceResponseInfoEntity> responseInfoEntityList = null;
        String sql = "";
        try {
            sql += " select *                         ";
            sql += "   from interface_response_info   ";
            sql += "  where projectId = :projectId    ";

            Map<String, Object> paramSql = new HashMap<String, Object>();
            paramSql.put("projectId", CommonUtils.objectToStr(projectId));

            responseInfoEntityList = jpaDao.queryListByParam(sql, InterfaceResponseInfoEntity.class, paramSql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseInfoEntityList;
    }
}
