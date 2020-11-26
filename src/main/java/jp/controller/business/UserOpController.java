package jp.controller.business;

import jp.anno.NoStandParam;
import jp.anno.StandJsonParam;
import jp.dto.LoginDto;
import jp.service.IUserService;
import jp.utils.ResultVoUtil;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping(value = "/test", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @StandJsonParam(module = "外部接口请求日志", methods = "测试日志")
    public ResultVo postMethod(HttpServletRequest request, HttpServletResponse response) {


        return ResultVoUtil.success("测试成功");
    }



}
