package jp.db.dao;

import jp.entity.DynamicParamEntity;

import java.util.List;
import java.util.Map;

public interface IDynamicParamDB {

    List<DynamicParamEntity> getDynamicParamList(Map<String, Object> param);
    int countDynamicParamList(Map<String, Object> param);
    int delDynamicParamById(String id);
}
