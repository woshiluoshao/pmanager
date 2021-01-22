package jp.controller.page;

import jp.db.dao.IDynamicParamDB;
import jp.entity.DynamicParamEntity;
import jp.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageRestController {

    @Autowired
    IDynamicParamDB dynamicParamDB;

    @RequestMapping(value = "/register")
    public ModelAndView registerPage(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");

        return modelAndView;
    }

    @RequestMapping(value = "/login")
    public String loginPage() {
        return "index";
    }

//    @RequestMapping(value = "/main")
//    public String mainPage() {
//        return "main";
//    }

    @RequestMapping(value = "/main")
    public ModelAndView mainPage(HttpServletRequest request, HttpServletResponse response) {

        String sessionUserId = CommonUtils.objectToStr(request.getSession().getAttribute("userId"));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("sessionUserId", sessionUserId);
        modelAndView.setViewName("main");

        return modelAndView;
    }


    @RequestMapping(value = "/userLog")
    public String logPage() {
        return "userLog";
    }

    @RequestMapping(value = "/chart")
    public String chatPage() {
        return "chart";
    }

    @RequestMapping(value = "/charts")
    public String chatsPage() {
        return "charts";
    }

    @RequestMapping(value = "/infoInter")
    public String infoInterPage(Model model) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("paramKey", "developmentLanguage");
        List<DynamicParamEntity> developmentLanguageList = dynamicParamDB.getDynamicParamList(param);

        param = new HashMap<String, Object>();
        param.put("paramKey", "developmentArchitect");
        List<DynamicParamEntity> developmentArchitectList = dynamicParamDB.getDynamicParamList(param);

        param = new HashMap<String, Object>();
        param.put("paramKey", "projectStatus");
        List<DynamicParamEntity> projectStatusList = dynamicParamDB.getDynamicParamList(param);

        model.addAttribute("languageEnum", developmentLanguageList);
        model.addAttribute("devArchitectEnum", developmentArchitectList);
        model.addAttribute("projectStatusEnum", projectStatusList);
        return "infoInter";
    }

    @RequestMapping(value = "/infoThird")
    public String infoThirdPage() {
        return "infoThird";
    }

    @RequestMapping(value = "/infoDeploy")
    public String infoDeployPage() {
        return "infoDeploy";
    }

    @RequestMapping(value = "/infoHandle")
    public String infoHandlePage() {
        return "infoHandle";
    }

    @RequestMapping(value = "/fileChange")
    public String fileChangePage() {
        return "fileChange";
    }

    @RequestMapping(value = "/fileCreate")
    public String fileCreatePage() {
        return "fileCreate";
    }
}
