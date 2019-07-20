package com.wishuok.controller;

import com.wishuok.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private IRedisService redisService;

    // http://localhost/redis/doTest
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String doTest(){
        String str = redisService.doTest();
        return str;     // 必须用 RestController， 否则会显示404页面
    }
    
}
