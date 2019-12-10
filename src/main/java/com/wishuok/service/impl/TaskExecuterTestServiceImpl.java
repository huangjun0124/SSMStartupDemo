package com.wishuok.service.impl;

import com.wishuok.pojo.Role;
import com.wishuok.service.TaskExecuterTestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class TaskExecuterTestServiceImpl implements TaskExecuterTestService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "asyncServiceExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void insertRoles(List<Role> roles) {
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        RoleService roleService = applicationContext.getBean(RoleService.class);
                        int ret = roleService.insertRole(role);
                        System.out.println("插入Role[" + role.getRoleName()+"]结果： " + ret + ",    当前线程id：  " + Thread.currentThread().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();  //这个不管是否异常都需要数量减,否则会被堵塞无法结束
                    }
                }
            });
        }
        try {
            countDownLatch.await(); //保证之前的所有的线程都执行完成，才会走下面的；
            // 这样就可以在下面拿到所有线程执行完的集合结果
            System.out.println("all roles insert done..........................................................");
        } catch (Exception e) {
            logger.error("阻塞异常");
        }
    }
}
