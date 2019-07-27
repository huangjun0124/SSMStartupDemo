package com.wishuok.controller;

import com.wishuok.utils.CookieHelper;
import com.wishuok.utils.JsonHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {

    // 两种写法都可以
    @RequestMapping(value = "", method = RequestMethod.GET)
   /* public String getLoginPage(){
          return "login";
    }*/
    public ModelAndView loginView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/login");
        return modelAndView;
    }

    //登录验证
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public ModelAndView loginVerify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberme = request.getParameter("rememberme");
        // 模拟登录校验
        if (!"wishuok".equals(username)) {
            modelAndView.setViewName("/login");
            modelAndView.addObject("message", "用户不存在！");
            InvalidateCookie(request, response);
            return modelAndView;
        } else if (!"pass".equals(password)) {
            modelAndView.setViewName("/login");
            modelAndView.addObject("message", "密码错误！");
            InvalidateCookie(request, response);
            return modelAndView;
        } else {
            //添加session
            request.getSession().setAttribute("LOGIN_USER", username);
            request.getSession().setMaxInactiveInterval(60 * 30);// 设置30分钟过期，需要重新登录
            //添加cookie
            if (rememberme != null) {
                CookieHelper.addCookie(response, "username", username);
                CookieHelper.addCookie(response, "password", password);
            }
            else{
                InvalidateCookie(request, response);
            }

            modelAndView.setViewName("redirect: /swagger-ui.html");
            return modelAndView;
        }
    }

    private void InvalidateCookie(HttpServletRequest request, HttpServletResponse response){
        CookieHelper.removeCookie(request, response, "username");
        CookieHelper.removeCookie(request, response, "password");
    }
}
