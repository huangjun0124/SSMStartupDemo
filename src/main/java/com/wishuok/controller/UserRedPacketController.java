package com.wishuok.controller;

import com.wishuok.service.IUserRedPacketService;
import com.wishuok.service.impl.UserRedPacketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/userRedPacket")
@Api(value = "/userRedPacket", tags = {"抢红包模拟"})
public class UserRedPacketController {

    @Autowired
    private IUserRedPacketService userRedPacketService = null;

    @RequestMapping(value = "/grab")
    @ResponseBody
    @ApiOperation(value = "带行锁的抢红包", notes = "获取行时加锁", httpMethod = "GET", response = Map.class)
    public Map<String, Object> grabRedPacket(int redPacketId, int userId)    {
        int result = userRedPacketService.grabRedPacketWithVersion(redPacketId, userId);
        Map<String, Object> retMap = new HashMap<String, Object>();
        boolean flag = result > 0;
        retMap.put("success", flag);
        retMap.put("message", flag ? "抢红包成功" : "抢红包失败");
        return retMap;
    }

    @RequestMapping(value = "/grabWithRedis")
    @ResponseBody
    @ApiOperation(value = "redis实现", notes = "利用redis抢红包", httpMethod = "GET", response = Map.class)
    public Map<String, Object> grabWithRedis(int redPacketId, int userId)    {
        int result = userRedPacketService.grabRedPacketByRedis(redPacketId, userId);
        Map<String, Object> retMap = new HashMap<String, Object>();
        boolean flag = result > 0;
        retMap.put("success", flag);
        String msg = flag ? "抢红包成功 " : "抢红包失败 ";
        msg += result;
        retMap.put("message",msg);
        return retMap;
    }
    
    // 返回jsp页面，调用url 示例：http://localhost/userRedPacket/doTest
    @RequestMapping(value = "/doTest")
    @ApiOperation(value = "nothing but test", notes = "返回string测试", httpMethod = "GET", response = String.class)
    public String doTest(){
        return "redPacketTest";
    }

}
