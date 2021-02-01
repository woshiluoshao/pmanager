package jp.controller.business;

import jp.anno.LoginAnnounce;
import jp.db.mybatis.model.UserAccountInfo;
import jp.dto.LoginDto;
import jp.service.IAccountService;
import jp.utils.Layui;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccountController {

    @Autowired
    IAccountService accountService;

    /**
     * 账号登录
     * @param loginDto
     * @return
     */
    @RequestMapping(value = "/accountLog.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @LoginAnnounce(module = "内部访问", methods = "用户登录")
    public ResultVo accountLoginMethod(LoginDto loginDto, HttpServletRequest request) {
        return accountService.accountLogin(loginDto, request);
    }

    /**
     * 账号分配
     * @param accountInfo
     * @return
     */
    @RequestMapping(value = "/accountAssign.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @LoginAnnounce(module = "内部访问", methods = "账号分配")
    public ResultVo accountAssignMethod(UserAccountInfo accountInfo) {
        return accountService.accountAssign(accountInfo);
    }

    /**
     * 账号删除
     * @param accountInfo
     * @return
     */
    @RequestMapping(value = "/accountDel.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @LoginAnnounce(module = "内部访问", methods = "账号删除")
    public ResultVo accountDelMethod(UserAccountInfo accountInfo) {
        return accountService.accountDel(accountInfo);
    }

    /**
     * 账号查询
     * @param request
     * @return
     */
    @RequestMapping(value = "/accountList.json", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Layui accountListMethod(HttpServletRequest request) {

        return accountService.accountList(request);
    }

    /**查询在线账号
     * @param request
     * @return
     */
    @RequestMapping(value = "/accountOnline.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo accountOnlineMethod(HttpServletRequest request) {
        return accountService.accountOnline(request);
    }

    /**修改账户信息
     * @param accountInfo
     * @return
     */
    @RequestMapping(value = "/accountUpdate.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo accountUpdateMethod(UserAccountInfo accountInfo, HttpServletRequest request) {
        return accountService.accountUpdate(accountInfo, request);
    }




}
