package com.wishuok.controller;

import com.wishuok.service.IRedisService;
import com.wishuok.utils.LogHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/redis")
@Api(value = "/redis", tags = {"redis连接测试"})
public class RedisController {

    @Autowired
    private IRedisService redisService;

    // http://localhost/redis/testString
    @RequestMapping(value = "/testString", method = RequestMethod.GET)
    @ApiOperation(value = "string test", notes = "测试 string 操作", httpMethod = "GET", response = String.class)
    public String testString() {
        LogHelper.LogInfo("start test redis string...");
        try {
            String str = redisService.testString();
            return str;     // 必须用 RestController， 否则会显示404页面
        } catch (Exception e) {
             LogHelper.LogError("redis test error:" + e.getMessage());
        }
        return "Error Ocurred, please try again later...";
    }

    //    http://localhost/redis/testHash
    @RequestMapping(value = "/testHash", method = RequestMethod.GET)
    @ApiOperation(value = "hash test", notes = "测试 hash 操作", httpMethod = "GET", response = String.class)
    public String testHash() {
        String str = redisService.testHash();
        return str;     // 必须用 RestController， 否则会显示404页面
    }

    //    http://localhost/redis/testList
    @RequestMapping(value = "/testList", method = RequestMethod.GET)
    @ApiOperation(value = "List test", notes = "测试 List 操作", httpMethod = "GET", response = String.class)
    public String testList() throws UnsupportedEncodingException {
        String str = redisService.testList();
        return str;     // 必须用 RestController， 否则会显示404页面
    }

    //    http://localhost/redis/testSet
    @RequestMapping(value = "/testSet", method = RequestMethod.GET)
    @ApiOperation(value = "Set test", notes = "测试 Set 操作", httpMethod = "GET", response = String.class)
    public String testSet() throws UnsupportedEncodingException {
        String str = redisService.testSet();
        return str;     // 必须用 RestController， 否则会显示404页面
    }

    //    http://localhost/redis/testSortedSet
    @RequestMapping(value = "/testSortedSet", method = RequestMethod.GET)
    @ApiOperation(value = "SortedSet test", notes = "测试 SortedSet 操作", httpMethod = "GET", response = String.class)
    public String testSortedSet() throws UnsupportedEncodingException {
        String str = redisService.testSortedSet();
        return str;     // 必须用 RestController， 否则会显示404页面
    }

    //    http://localhost/redis/testPipeline
    @RequestMapping(value = "/testPipeline", method = RequestMethod.GET)
    @ApiOperation(value = "Pipeline test", notes = "测试 Pipeline ", httpMethod = "GET", response = String.class)
    public String testPipeline() throws UnsupportedEncodingException {
        String str = redisService.testPipeline();
        return str;     // 必须用 RestController， 否则会显示404页面
    }
}
