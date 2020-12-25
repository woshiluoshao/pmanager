package jp.controller.business;

import jp.service.IInterfaceService;
import jp.utils.Layui;
import jp.utils.ResultVoUtil;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class InterfaceController {

    @Autowired
    IInterfaceService interfaceService;

    //region 接口信息操作

    /**
     * 查询接口信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/interfaceSelect.json", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Layui interfaceSelectMethod(HttpServletRequest request) {
        return interfaceService.selectPerResInterface(request);
    }

    /**
     * 接口增加
     * @param request
     * @return
     */
    @RequestMapping(value = "/interfaceAdd.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo interfaceAddMethod(HttpServletRequest request) {
        return interfaceService.addPerResInterface(request);
    }

    /**
     * 接口删除
     * @param request
     * @return
     */
    @RequestMapping(value = "/interfaceDel.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo interfaceDelMethod(HttpServletRequest request) {
        return interfaceService.delPerResInterface(request);
    }

    //endregion

    //region部署接口操作

    @RequestMapping(value = "/addDeploy.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo addDeployMethod(HttpServletRequest request) {
        return ResultVoUtil.success();
    }

    //endregion
}
