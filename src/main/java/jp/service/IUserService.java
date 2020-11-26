package jp.service;

import jp.dto.LoginDto;
import jp.vo.ResultVo;

public interface IUserService {

    ResultVo loginExe(LoginDto loginDto);
    ResultVo registerExe(LoginDto loginDto);
}
