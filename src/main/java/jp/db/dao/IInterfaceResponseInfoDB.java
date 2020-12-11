package jp.db.dao;

import jp.entity.InterfaceResponseInfoEntity;

import java.util.List;
import java.util.Map;

public interface IInterfaceResponseInfoDB {

    List<InterfaceResponseInfoEntity> getPersonResList(Map<String, Object> param);
    int countPerResList(Map<String, Object> param);
    int delPersonRes(String director, String project);
    int delBatchPersonRes(String director, String projectList);
    int updatePersonRes(InterfaceResponseInfoEntity personResInterfaceEntity);
    int addPersonRes(InterfaceResponseInfoEntity personResInterfaceEntity);
    List<InterfaceResponseInfoEntity> getPersonResByKey(String director, String project);



}
