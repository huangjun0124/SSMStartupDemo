package com.wishuok.service.impl;

import com.wishuok.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("redisService")
public class RedisService implements IRedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 使用 json 做序列化时，string【xxx】会被序列化为"xxx"，多两个引号，造成长度位置相关运算不准确，并且字符串内部有引号
    //      时取出的串被截断问题（见下面返回值）
    // 解决办法：使用  stringRedisSerializer 作为 valueSerializer
    @Override
    public String testString() {
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
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();

        return newV;
    }

    @Override
    public String testHash() {
        String key = "hash";
        Map<String, String> map = new HashMap<String, String>();
        map.put("f1", "val1");
        map.put("f2", "val2");
        redisTemplate.opsForHash().putAll(key, map); // hmset
        redisTemplate.opsForHash().put(key, "f3", "6");
        boolean exist = redisTemplate.opsForHash().hasKey(key, "f3");
        Map keyValMap = redisTemplate.opsForHash().entries(key);
        redisTemplate.opsForHash().increment(key, "f3", 9.88);
        List valueList = redisTemplate.opsForHash().values(key);
        Set keyList = redisTemplate.opsForHash().keys(key);
        boolean success = redisTemplate.opsForHash().putIfAbsent(key, "f3", "9999999"); // value 必须是 string， 因为用了 stringRedisSerializer
        return "nothing but test";
    }

    @Override
    public String testList() throws UnsupportedEncodingException {
        redisTemplate.delete("list1");
        redisTemplate.delete("list2");
        //初始化链表 list
        List<String> nodeList = new ArrayList<String>();
        for (int i = 1; i <= 5; i++) {
            nodeList.add("node" + i);
        }
        redisTemplate.opsForList().leftPushAll("list1", nodeList);
        //Spring 使用参数超时时间作为阻塞命令区分，等价于 blpop 命令，并且可以设置时间参数
        redisTemplate.opsForList().leftPop("list1", 1, TimeUnit.SECONDS);
        //Spring 使用参数超时时间作为阻塞命令区分，等价于 brpop 命令，并且可以设置时间参数
        redisTemplate.opsForList().rightPop("list1", 1, TimeUnit.SECONDS);
        nodeList.clear();
        //初始化链表 list2
        for (int i = 1; i <= 3; i++) {
            nodeList.add("data" + i);
            redisTemplate.opsForList().leftPushAll("list1", nodeList);
            //相当于 rpoplpush 命令，弹出 listl 最右边的节点，插入到 list2 最左边
            redisTemplate.opsForList().rightPopAndLeftPush("list1", "list2");
            redisTemplate.opsForList().leftPush("list1","left"+i);
            //相当于 brpoplpush 命令，注意在 Spring 中使用超时参数区分
            redisTemplate.opsForList().rightPopAndLeftPush("list1", "list2", 1, TimeUnit.SECONDS);

        }
        redisTemplate.opsForList().leftPushAll("list1", nodeList);
        // 使用 linsert 命令在node2 前插入一个节点
        redisTemplate.getConnectionFactory().getConnection().lInsert("list2".getBytes(StandardCharsets.UTF_8), RedisListCommands.Position.BEFORE,
                "node2".getBytes(StandardCharsets.UTF_8), "before_inserted".getBytes(StandardCharsets.UTF_8));
        //链表长度
        Long size = redisTemplate.opsForList().size("list2");
        // 获取整个链表的值
        List valueList = redisTemplate.opsForList().range("list2", 0, size);

        return "list test";
    }

    @Override
    public String testSet() {
        Set set = null;
        redisTemplate.opsForSet().add("set1","v1","v2","v3","v4","v5","v6");
        redisTemplate.opsForSet().add("set2","v0","v2","v4","v6","v8");
        long size = redisTemplate.opsForSet().size("set1");
        // 差集
        set = redisTemplate.opsForSet().difference("set1","set2");
        // 交集
        set = redisTemplate.opsForSet().intersect("set1", "set2");
        // 是否集合元素
        boolean isExist = redisTemplate.opsForSet().isMember("set1","v2");

        // 随机弹出一个元素
        Object val = redisTemplate.opsForSet().pop("set2");
        // 获取集合元素
        set = redisTemplate.opsForSet().members("set2");
        // 随机获取2个元素
        val = redisTemplate.opsForSet().randomMembers("set2",2L);
        // 删除元素
        redisTemplate.opsForSet().remove("set1","v1");
        // 并集
        set = redisTemplate.opsForSet().union("set1", "set2");
        // 求交集并存到另一个集合
        redisTemplate.opsForSet().intersectAndStore("set1", "set2","inter_set");
        
        return "test set";
    }

    @Override
    public String testSortedSet() {
        Set<ZSetOperations.TypedTuple> set1 = new HashSet<ZSetOperations.TypedTuple>();
        Set<ZSetOperations.TypedTuple> set2 = new HashSet<ZSetOperations.TypedTuple>();
        int j = 9;
        for (int i = 0; i <= 9; i++){
            j--;
            Double score1 = Double.valueOf(i);
            String value1 = "x" + i;
            Double score2 = Double.valueOf(j);
            String value2 = j % 2 == 1 ? "y" + j : "x" + j;
            ZSetOperations.TypedTuple tuple1 = new DefaultTypedTuple(value1, score1);
            ZSetOperations.TypedTuple tuple2 = new DefaultTypedTuple(value2, score2);
            set1.add(tuple1);
            set2.add(tuple2);
        }
        redisTemplate.opsForZSet().add("zset1", set1);
        redisTemplate.opsForZSet().add("zset2", set2);
         Long size = null;
         // 总数
         size = redisTemplate.opsForZSet().zCard("zset1");
         // 求分数在 3<= score <= 6 范围的个数
        size = redisTemplate.opsForZSet().count("zset1",3,6);
        // 从下标开始截取4个元素,不返回分数
         Set set = redisTemplate.opsForZSet().range("zset1",2,5);
         // 返回元素和分数   end 传-1则获取全部元素
         set = redisTemplate.opsForZSet().rangeWithScores("zset1", 2, 4);
         size = redisTemplate.opsForZSet().intersectAndStore("zset1","zset2","zset-inter");
         // 区间
        RedisZSetCommands.Range range = RedisZSetCommands.Range.range();
        RedisZSetCommands.Limit limit = RedisZSetCommands.Limit.limit();
        limit.offset(2);
        limit.count(3);
        set = redisTemplate.opsForZSet().rangeByLex("zset1",range,limit);
         // 排名
        Long rank = redisTemplate.opsForZSet().rank("zset1","x3");

        return "test Sorted Set";
    }
}

