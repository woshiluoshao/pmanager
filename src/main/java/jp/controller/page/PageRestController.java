package jp.controller.page;

import jp.utils.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PageRestController {

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

    @RequestMapping(value = "/proDetail")
    public String proDetailPage() {
        return "proDetail";
    }

}
