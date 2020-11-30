package jp.controller.business;

import jp.anno.NoStandParam;
import jp.dto.LoginDto;
import jp.service.IUserService;
import jp.utils.Layui;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserOpController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/login.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @NoStandParam(module = "内部访问日志", methods = "用户登录")
    public ResultVo loginMethod(LoginDto loginDto) {
        return userService.loginExe(loginDto);
    }

    @RequestMapping(value = "/register.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @NoStandParam(module = "内部访问日志", methods = "用户注册")
    public ResultVo registerMethod(LoginDto loginDto) {
        return userService.registerExe(loginDto);
    }

    @RequestMapping(value = "/selectLog.json", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Layui selectLogMethod(HttpServletRequest request) {
        return userService.selectLogAll(request);
    }



}
