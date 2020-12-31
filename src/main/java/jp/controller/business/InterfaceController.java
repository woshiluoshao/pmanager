package jp.controller.business;

import jp.service.IDeployService;
import jp.service.IInterfaceService;
import jp.utils.Layui;
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

    @Autowired
    IDeployService deployService;

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

    /**
     * 发布部署
     * @param request
     * @return
     */
    @RequestMapping(value = "/addDeploy.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo addDeployMethod(HttpServletRequest request) {

        return deployService.addDeploy(request);
    }

    /**
     * 获取我发布的部署
     * @param request
     */
    @RequestMapping(value = "/getDeploy.json", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Layui getDeployMethod(HttpServletRequest request) {

         return deployService.getMyDeploy(request);
    }

    //endregion
}
