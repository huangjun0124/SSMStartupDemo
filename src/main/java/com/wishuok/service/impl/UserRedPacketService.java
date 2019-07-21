package com.wishuok.service.impl;

import com.wishuok.mapper.RedPacketMapper;
import com.wishuok.mapper.UserRedPacketMapper;
import com.wishuok.pojo.RedPacket;
import com.wishuok.pojo.UserRedPacket;
import com.wishuok.service.IUserRedPacketService;
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
        RedPacket redPacket = redPacketMapper.selectByPrimaryKey(redPacketId);
        if(redPacket.getStock() > 0){
            redPacketMapper.decreaseRedPacket(redPacketId);
            UserRedPacket userRedPacket = new UserRedPacket();
            userRedPacket.setRedPacketId(redPacketId);
            userRedPacket.setAmount(redPacket.getUnitAmount());
            userRedPacket.setUserId(userId);
            userRedPacket.setGrabTime(new Date());
            userRedPacket.setNote("抢红包" + redPacketId);

            int result = userRedPacketMapper.insert(userRedPacket);
            return result;
        }
        return FAILED;
    }
}
