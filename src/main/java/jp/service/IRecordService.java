package jp.service;

import jp.db.mybatis.model.RecordThirdPartWithBLOBs;
import jp.utils.Layui;
import jp.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;

public interface IRecordService {

    ResultVo addThirdPart(RecordThirdPartWithBLOBs thirdPartWithBLOBs, HttpServletRequest request);
    Layui selectThirdPart(HttpServletRequest request);
    ResultVo delThirdPart(HttpServletRequest request);
}
