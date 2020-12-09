package jp.db.dao;

import jp.entity.PersonResInterfaceEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IPersonResInterDB {

    List<PersonResInterfaceEntity> getPersonResList(Map<String, Object> param);
    int countPerResList(Map<String, Object> param);
    int delPersonRes(String director, String project);
    int delBatchPersonRes(String director, String projectList);
    int updatePersonRes(PersonResInterfaceEntity personResInterfaceEntity);
    int addPersonRes(PersonResInterfaceEntity personResInterfaceEntity);
    List<PersonResInterfaceEntity> getPersonResByKey(String director, String project);



}
