package com.wishuok.service.impl;

import com.wishuok.mapper.RoleMapper;
import com.wishuok.pojo.Role;
import com.wishuok.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


//添加Service注解，让Spring进行实例化管理，并给该实例化对象取一个名字，后面使用
@Service("roleService")
public class RoleService implements IRoleService {

    //注入需要的Mapper实例，用于方法调用
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role findRoleById(String id) {
        testJedisPool();
        return roleMapper.selectByPrimaryKey(id);
    }

    private void testJedis(){
        Jedis jedis = new Jedis("192.168.109.128", 6379);

        jedis.auth("redis");
        int i= 0 ;
        try {
            long start = System.currentTimeMillis();
            while (true) {
                long end = System.currentTimeMillis();
                if (end - start >= 1000) {
                    break;
                }
                i++;
                jedis.set("test" + i, i + "");

            }
        }finally {
            jedis.close();
        }
    }

    private void testJedisPool(){
        JedisPoolConfig poolCfg = new JedisPoolConfig();
        poolCfg.setMaxIdle(50); // 最大空闲数
        poolCfg.setMaxTotal(100); // 最大连接数
        poolCfg.setMaxWaitMillis(20000); // 最大等待毫秒数
        JedisPool jp = new JedisPool(poolCfg, "192.168.109.128",6379,2000,"redis",0);
        Jedis jedis = jp.getResource();
        for (int i=0; i<2994;i++){
            if(jedis.exists("test"+i)){
                jedis.del("test"+i);
            }
        }
    }
}
