package com.wishuok.service.impl;

import com.sun.org.apache.bcel.internal.generic.FALOAD;
import com.wishuok.mapper.RedPacketMapper;
import com.wishuok.mapper.UserRedPacketMapper;
import com.wishuok.pojo.RedPacket;
import com.wishuok.pojo.UserRedPacket;
import com.wishuok.service.IUserRedPacketService;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        if(redPacket.getStock() > 0){
            redPacketMapper.decreaseRedPacket(redPacketId);
            return insertUserRedPacketInfo(redPacket, userId);
        }
        return FAILED;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grabRedPacketWithVersion(int redPacketId, int userId) {
        long startTime = System.currentTimeMillis();
        while(true){
             long endTime = System.currentTimeMillis();
             if(endTime-startTime > 300){  // 超过300ms，则返回失败
                 return FAILED;
             }
            RedPacket redPacket = redPacketMapper.selectByPrimaryKey(redPacketId);
            if(redPacket.getStock() > 0){
                // 传入当前线程获取到的 stock 和 version，检查数据库中该数据是否已被别人更新过
                int update = redPacketMapper.decreaseRedPackWithVersion(redPacketId,redPacket.getVersion(),redPacket.getStock());
                if(update == 0){
                    // 没有数据更新，说明其他线程已经修改过数据，本次抢红包失败
                    continue;
                }
                return insertUserRedPacketInfo(redPacket, userId);
            }
            return FAILED;
        }
    }

    private int insertUserRedPacketInfo(RedPacket redPacket, int userId){
        UserRedPacket userRedPacket = new UserRedPacket();
        userRedPacket.setRedPacketId(redPacket.getId());
        userRedPacket.setAmount(redPacket.getUnitAmount());
        userRedPacket.setUserId(userId);
        userRedPacket.setGrabTime(new Date());
        userRedPacket.setNote("抢红包" + redPacket.getId());

        return userRedPacketMapper.insert(userRedPacket);
    }
}
