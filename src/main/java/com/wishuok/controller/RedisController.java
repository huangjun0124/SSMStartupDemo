package com.wishuok.controller;

import com.wishuok.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private IRedisService redisService;

    // http://localhost/redis/testString
    @RequestMapping(value = "/testString", method = RequestMethod.GET)
    public String testString(){
        String str = redisService.testString();
        return str;     // 必须用 RestController， 否则会显示404页面
    }

    //    http://localhost/redis/testHash
    @RequestMapping(value = "/testHash", method = RequestMethod.GET)
    public String testHash(){
        String str = redisService.testHash();
        return str;     // 必须用 RestController， 否则会显示404页面
    }

    //    http://localhost/redis/testList
    @RequestMapping(value = "/testList", method = RequestMethod.GET)
    public String testList() throws UnsupportedEncodingException {
        String str = redisService.testList();
        return str;     // 必须用 RestController， 否则会显示404页面
    }

    //    http://localhost/redis/testSet
    @RequestMapping(value = "/testSet", method = RequestMethod.GET)
    public String testSet() throws UnsupportedEncodingException {
        String str = redisService.testSet();
        return str;     // 必须用 RestController， 否则会显示404页面
    }

    //    http://localhost/redis/testSortedSet
    @RequestMapping(value = "/testSortedSet", method = RequestMethod.GET)
    public String testSortedSet() throws UnsupportedEncodingException {
        String str = redisService.testSortedSet();
        return str;     // 必须用 RestController， 否则会显示404页面
    }

    //    http://localhost/redis/testPipeline
    @RequestMapping(value = "/testPipeline", method = RequestMethod.GET)
    public String testPipeline() throws UnsupportedEncodingException {
        String str = redisService.testPipeline();
        return str;     // 必须用 RestController， 否则会显示404页面
    }
}
