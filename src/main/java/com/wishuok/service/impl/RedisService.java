package com.wishuok.service.impl;

import com.wishuok.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service("redisService")
public class RedisService implements IRedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 使用 json 做序列化时，string【xxx】会被序列化为"xxx"，多两个引号，造成长度位置相关运算不准确，并且字符串内部有引号
    //      时取出的串被截断问题（见下面返回值）
    // 解决办法：使用  stringRedisSerializer 作为 valueSerializer
    @Override
    public String doTest() {
        redisTemplate.opsForValue().set("key1", "nothingbuttest1");
        redisTemplate.opsForValue().set("key2", "nothingbuttest2019");
        redisTemplate.delete("key1");
        // json 序列化，写入的redis value 为【"nothingbuttest2019"】(带引号，所以长度不是18)
        Object len = redisTemplate.opsForValue().size("key2");
        len = redisTemplate.opsForValue().size("key1"); // 0
        String oldV = (String) redisTemplate.opsForValue().getAndSet("key2", "lala2");
        String newV = (String) redisTemplate.opsForValue().get("key2");
        len = redisTemplate.opsForValue().size("key2"); // 0
        // getRange(rawKey, start, end)
        String subStr = redisTemplate.opsForValue().get("key2", 0, 3); // "lal
        int newLen = redisTemplate.opsForValue().append("key2", "pockman");
        newV = (String) redisTemplate.opsForValue().get("key2");   //lala2

        // 获取原始 Jedis 对象，支持更多redis 操作
        Jedis jedis = (Jedis)redisTemplate.getConnectionFactory().getConnection().getNativeConnection();

        return newV;
    }
}
