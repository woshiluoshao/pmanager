package jp.service;

import jp.dto.LoginDto;
import jp.utils.Layui;
import jp.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {

    ResultVo loginExe(LoginDto loginDto, HttpServletRequest request);
    ResultVo registerExe(LoginDto loginDto);
    ResultVo selectUserList(HttpServletRequest request);
    Layui selectLogAll(HttpServletRequest request);

}
