package jp.service;

import jp.utils.Layui;
import jp.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;

public interface IDeployService {

    ResultVo addDeploy(HttpServletRequest request);
    Layui getMyDeploy(HttpServletRequest request);
    Layui getMyAudit(HttpServletRequest request);
    ResultVo handleAudit(HttpServletRequest request);
    Layui getSendRecord(HttpServletRequest request);
    Layui getHandleRecord(HttpServletRequest request);
}
