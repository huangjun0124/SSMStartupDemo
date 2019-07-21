package com.wishuok.service.impl;

import com.sun.org.apache.bcel.internal.generic.FALOAD;
import com.wishuok.mapper.RedPacketMapper;
import com.wishuok.mapper.UserRedPacketMapper;
import com.wishuok.pojo.RedPacket;
import com.wishuok.pojo.UserRedPacket;
import com.wishuok.service.IRedisRedPacketService;
import com.wishuok.service.IUserRedPacketService;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;

@Service("userRedPacketService")
public class UserRedPacketService implements IUserRedPacketService {
    @Autowired
    private UserRedPacketMapper userRedPacketMapper = null;

    @Autowired
    private RedPacketMapper redPacketMapper = null;

    private static final int FAILED = -1;

    // 事物隔离级别： 读/写提交
    // 传播行为：调用方法时，若没有事物，则创建事物，否则沿用当前事物
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grabRedPacket(int redPacketId, int userId) {
        // 无锁，并发量高会导致超发问题
        // RedPacket redPacket = redPacketMapper.selectByPrimaryKey(redPacketId);
        // 悲观锁，select会加行锁，有性能问题
        RedPacket redPacket = redPacketMapper.selectByPrimariKeyForUpdate(redPacketId);
        if (redPacket.getStock() > 0) {
            redPacketMapper.decreaseRedPacket(redPacketId);
            return insertUserRedPacketInfo(redPacket, userId);
        }
        return FAILED;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grabRedPacketWithVersion(int redPacketId, int userId) {
        long startTime = System.currentTimeMillis();
        while (true) {
            long endTime = System.currentTimeMillis();
            if (endTime - startTime > 300) {  // 超过300ms，则返回失败
                return FAILED;
            }
            RedPacket redPacket = redPacketMapper.selectByPrimaryKey(redPacketId);
            if (redPacket.getStock() > 0) {
                // 传入当前线程获取到的 stock 和 version，检查数据库中该数据是否已被别人更新过
                int update = redPacketMapper.decreaseRedPackWithVersion(redPacketId, redPacket.getVersion(), redPacket.getStock());
                if (update == 0) {
                    // 没有数据更新，说明其他线程已经修改过数据，本次抢红包失败
                    continue;
                }
                return insertUserRedPacketInfo(redPacket, userId);
            }
            return FAILED;
        }
    }

    @Autowired
    private RedisTemplate stringRedisTemplate = null;
    @Autowired
    private IRedisRedPacketService redisRedPacketService = null;
    //抢红包的LUA脚本
    private static final String RedisRedPacketLuaScript = "local listKey = 'red_packet_list_'..KEYS[1] \n"
            + " local redPacket = 'red_packet_'.. KEYS[1] \n"
            + " local stock= tonumber(redis.call('hget', redPacket,'stock'))"
            + " if stock <= 0 then return 0 end \n"
            + " stock = stock - 1 \n "
            + " redis.call('hset', redPacket,'stock', tostring(stock)) \n"
            + " redis.call('rpush', listKey, ARGV[1]) \n"
            + " if stock == 0 then return 2 end \n"
            + " return 1 \n";
    // 缓存lua脚本后得到的 sha1 值，用来调用脚本
    private static String RedisLuaScriptSha1 = null;

    /**
     * 通过 redis 实现抢红包
     *
     * @param redPacketId
     * @param userId
     * @return 0-没有库存，失败；1-成功，且不是最后一个红包；2-成功且是最后一个红包
     */
    @Override
    public int grabRedPacketByRedis(int redPacketId, int userId) {
        String args = userId + "-" + System.currentTimeMillis();
        int result = 0;
        Jedis jedis = (Jedis)stringRedisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        try{
            if(RedisLuaScriptSha1 == null) {// 加载脚本
                RedisLuaScriptSha1 = jedis.scriptLoad(RedisRedPacketLuaScript);
            }
            long res = (Long)jedis.evalsha(RedisLuaScriptSha1, 1, redPacketId + "", args);
            result =  (int)res;
            if(result == 2){
                // 最后一个红包
                String unitAmout = jedis.hget("red_packet_"+redPacketId, "unit_amount");
                double unitAmount = Double.parseDouble(unitAmout);
                System.err.println("thread_name = " + Thread.currentThread().getName());
                redisRedPacketService.saveUserRedPacketByRedis(redPacketId, unitAmount);
            }
        } finally {
            if(jedis != null && jedis.isConnected())
                jedis.close();
        }
        return result;
    }

    private int insertUserRedPacketInfo(RedPacket redPacket, int userId) {
        UserRedPacket userRedPacket = new UserRedPacket();
        userRedPacket.setRedPacketId(redPacket.getId());
        userRedPacket.setAmount(redPacket.getUnitAmount());
        userRedPacket.setUserId(userId);
        userRedPacket.setGrabTime(new Date());
        userRedPacket.setNote("抢红包" + redPacket.getId());

        return userRedPacketMapper.insert(userRedPacket);
    }
}
