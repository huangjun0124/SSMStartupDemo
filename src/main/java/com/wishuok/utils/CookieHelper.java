package com.wishuok.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHelper {
    
    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int expiry){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        //设置Cookie的有效期为3天
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }

    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        //默认有效期一天
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
    }

    public static boolean removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    return true;
                }

            }
        }
        return false;
    }

    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
