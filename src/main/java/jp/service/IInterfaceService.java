package jp.service;

import jp.utils.Layui;
import jp.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;

public interface IInterfaceService {

    ResultVo addPerResInterface(HttpServletRequest request);
    Layui selectPerResInterface(HttpServletRequest request);
    ResultVo delPerResInterface(HttpServletRequest request);

}
