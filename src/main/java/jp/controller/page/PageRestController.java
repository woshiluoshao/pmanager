package jp.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PageRestController {

    @RequestMapping(value = "/register")
    public ModelAndView signPage(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");

        return modelAndView;
    }

    @RequestMapping(value = "/login")
    public String loginPage() {
        return "index";
    }

    @RequestMapping(value = "/main")
    public String mainPage() {
        return "main";
    }

    @RequestMapping(value = "/userLog")
    public String logPage() {
        return "userLog";
    }

}
