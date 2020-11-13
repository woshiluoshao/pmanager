package jp.controller;

import jp.anno.NoStandParam;
import jp.anno.StandJsonParam;
import jp.dto.LoginDto;
import jp.enums.MessageEnum;
import jp.utils.ResultVoUtil;
import jp.vo.ResultVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @NoStandParam(description = "登录")
    public ResultVo loginMethod(LoginDto loginDto) {

        if(loginDto == null || StringUtils.isEmpty(loginDto.getUserId()) || StringUtils.isEmpty(loginDto.getPassword())) {
            System.out.println("参数不完整");
            return ResultVoUtil.error(MessageEnum.E002, null, "账号", "密码");
        }
        System.out.println("登录成功");
        return ResultVoUtil.success("登录成功");
    }

    @RequestMapping(value = "/test", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @StandJsonParam(description = "测试")
    public ResultVo postMethod(HttpServletRequest request, HttpServletResponse response) {


        return ResultVoUtil.success("测试成功");
    }



}
