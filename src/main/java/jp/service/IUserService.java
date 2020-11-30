package jp.service;

import jp.dto.LoginDto;
import jp.utils.Layui;
import jp.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {

    ResultVo loginExe(LoginDto loginDto);
    ResultVo registerExe(LoginDto loginDto);
    Layui selectLogAll(HttpServletRequest request);
}
