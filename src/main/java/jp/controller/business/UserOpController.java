package jp.controller.business;

import jp.anno.InnerAnnounce;
import jp.anno.LoginAnnounce;
import jp.dto.LoginDto;
import jp.service.IInterfaceService;
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

    @Autowired
    IInterfaceService interfaceService;

    /**
     * 登录
     * @param loginDto
     * @return
     */
    @RequestMapping(value = "/login.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @LoginAnnounce(module = "内部访问", methods = "用户登录")
    public ResultVo loginMethod(LoginDto loginDto, HttpServletRequest request) {
        return userService.loginExe(loginDto, request);
    }

    /**
     * 注册
     * @param loginDto
     * @return
     */
    @RequestMapping(value = "/register.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @InnerAnnounce(module = "内部访问", methods = "用户注册")
    public ResultVo registerMethod(LoginDto loginDto) {
        return userService.registerExe(loginDto);
    }

    /**
     * 用户登录注册退出日志查询
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectLog.json", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Layui selectLogMethod(HttpServletRequest request) {
        return userService.selectLogAll(request);
    }

    /**
     * 查询用户列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectOnline.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo selectUserListMethod(HttpServletRequest request) {
        return userService.selectUserList(request);
    }


    //region 接口信息操作

    @RequestMapping(value = "/interfaceSelect.json", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Layui interfaceSelectMethod(HttpServletRequest request) {
        return interfaceService.selectPerResInterface(request);
    }


    @RequestMapping(value = "/interfaceAdd.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo interfaceAddMethod(HttpServletRequest request) {
        return interfaceService.addPerResInterface(request);
    }


    //endregion


}
