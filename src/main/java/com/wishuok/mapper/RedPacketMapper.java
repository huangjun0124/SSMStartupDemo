package com.wishuok.mapper;

import com.wishuok.pojo.RedPacket;

public interface RedPacketMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RedPacket record);

    int insertSelective(RedPacket record);

    RedPacket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedPacket record);

    int updateByPrimaryKey(RedPacket record);

    /*
     * 扣减抢红包数
     * @param id -- 红包id
     * @return 更新吧记录条数
     */
    int decreaseRedPacket(int id);
}