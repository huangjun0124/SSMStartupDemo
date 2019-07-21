package com.wishuok.controller;

import com.wishuok.service.IUserRedPacketService;
import com.wishuok.service.impl.UserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/userRedPacket")
public class UserRedPacketController {

    @Autowired
    private IUserRedPacketService userRedPacketService = null;

    @RequestMapping(value = "/grab")
    @ResponseBody
    public Map<String, Object> grabRedPacket(int redPacketId, int userId)    {
        int result = userRedPacketService.grabRedPacket(redPacketId, userId);
        Map<String, Object> retMap = new HashMap<String, Object>();
        boolean flag = result > 0;
        retMap.put("success", flag);
        retMap.put("message", flag ? "抢红包成功" : "抢红包失败");
        return retMap;
    }

    // 调用url 示例：http://localhost/userRedPacket/doTest
    @RequestMapping(value = "/doTest")
    public String doTest(){
        return "redPacketTest";
    }

}
