package com.wishuok.service;

public interface IRedisRedPacketService {

    /** 保存 redis 抢红包列表
     * @param redPacketId   --红包编号
     * @param unitAmout     --红包金额
     */
    void saveUserRedPacketByRedis(int redPacketId, double unitAmout);
}
