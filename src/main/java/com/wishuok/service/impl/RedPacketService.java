package com.wishuok.service.impl;

import com.wishuok.mapper.RedPacketMapper;
import com.wishuok.pojo.RedPacket;
import com.wishuok.service.IRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class RedPacketService implements IRedPacketService {
    @Autowired
    private RedPacketMapper redPacketMapper = null;

    // 事物隔离级别： 读/写提交
    // 传播行为：调用方法时，若没有事物，则创建事物，否则沿用当前事物
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public RedPacket getRedPacket(int id) {
        return redPacketMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int decreaseRedPacket(int id) {
        return redPacketMapper.decreaseRedPacket(id);
    }
}
