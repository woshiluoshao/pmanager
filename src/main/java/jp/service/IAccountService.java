package jp.service;

import jp.db.mybatis.model.UserAccountInfo;
import jp.dto.LoginDto;
import jp.utils.Layui;
import jp.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;

public interface IAccountService {

    ResultVo accountLogin(LoginDto loginDto, HttpServletRequest request);
    ResultVo accountAssign(UserAccountInfo accountInfo);
    ResultVo accountDel(UserAccountInfo accountInfo);
    Layui accountList(HttpServletRequest request);
    ResultVo accountOnline(HttpServletRequest request);
    ResultVo accountUpdate(UserAccountInfo accountInfo, HttpServletRequest request);
}
