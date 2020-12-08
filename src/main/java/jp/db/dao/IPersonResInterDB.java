package jp.db.dao;

import jp.entity.PersonResInterfaceEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IPersonResInterDB {

    List<PersonResInterfaceEntity> getPersonResList(Map<String, Object> param);
    int countPerResList(Map<String, Object> param);
    int delPersonRes(String userId, String project);
    int updatePersonRes(PersonResInterfaceEntity personResInterfaceEntity);
    int addPersonRes(PersonResInterfaceEntity personResInterfaceEntity);
}
