package jp.service;

import jp.utils.Layui;
import jp.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;

public interface IRecordService {

    ResultVo addThirdPart(HttpServletRequest request);
    Layui selectThirdPart(HttpServletRequest request);
}
