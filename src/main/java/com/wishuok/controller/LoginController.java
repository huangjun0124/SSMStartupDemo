package com.wishuok.controller;

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
    @RequestMapping(value = "/doLogin",method = RequestMethod.POST)
    @ResponseBody
    public String loginVerify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberme = request.getParameter("rememberme");
        // 模拟登录校验
        if(!username.equals("wishuok")) {
            map.put("code",-1);
            map.put("msg","用户名无效！");
        } else if(!password.equals("pass")) {
            map.put("code",-1);
            map.put("msg","密码错误！");
        } else {
            //登录成功
            map.put("code",0);
            map.put("msg","");
            //添加session
            request.getSession().setAttribute("LOGIN_USER", username);
            request.getSession().setMaxInactiveInterval(60 * 30);// 设置30分钟过期，需要重新登录
            //添加cookie
            if(rememberme!=null) {
                //创建两个Cookie对象
                Cookie nameCookie = new Cookie("username", username);
                //设置Cookie的有效期为3天
                nameCookie.setMaxAge(60 * 60 * 24 * 3);
                Cookie pwdCookie = new Cookie("password", password);
                pwdCookie.setMaxAge(60 * 60 * 24); // 1 天
                response.addCookie(nameCookie);
                response.addCookie(pwdCookie);
            }
        }
        return JsonHelper.toJson(map);
    }
}
