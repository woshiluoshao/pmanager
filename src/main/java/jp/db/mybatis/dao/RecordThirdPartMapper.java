package jp.db.mybatis.dao;

import jp.db.mybatis.model.RecordThirdPart;
import jp.db.mybatis.model.RecordThirdPartWithBLOBs;

import java.util.List;
import java.util.Map;

public interface RecordThirdPartMapper {
    int deleteByPrimaryKey(String uuid);

    int insert(RecordThirdPartWithBLOBs record);

    int insertSelective(RecordThirdPartWithBLOBs record);

    RecordThirdPartWithBLOBs selectByPrimaryKey(String uuid);

    int updateByPrimaryKeySelective(RecordThirdPartWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(RecordThirdPartWithBLOBs record);

    int updateByPrimaryKey(RecordThirdPart record);

    int countThirdPartList(Map<String, Object> param);

    List<RecordThirdPartWithBLOBs> getThirdPartList(Map<String, Object> param);
}