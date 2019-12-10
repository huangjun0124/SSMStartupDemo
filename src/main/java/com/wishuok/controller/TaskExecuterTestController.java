package com.wishuok.controller;

import com.wishuok.pojo.Role;
import com.wishuok.service.TaskExecuterTestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/task")
@Api(value = "/task", tags = {"TaskExecuter测试"})
public class TaskExecuterTestController {

    @Autowired
    private TaskExecuterTestService taskExecuterTestService;

    @RequestMapping(value = "/doTest", method = RequestMethod.GET)
    @ResponseBody
    public String doTest() {
        StopWatch sc = new StopWatch();
        sc.start();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        List<Role> roles = new ArrayList<Role>();
        int totalCount = 100000;
        for (int i = 0; i < totalCount; i++) {
            Role role = new Role();
            role.setId(UUID.randomUUID().toString());
            role.setRoleName("testRole_" + i);
            role.setNote(ft.format(new Date()));
            roles.add(role);
        }
        taskExecuterTestService.insertRoles(roles);
        sc.stop();
        return "测试完成，插入数据 "+totalCount+" 条，总耗时 " + sc.getTotalTimeMillis() + " 毫秒";
    }

}
