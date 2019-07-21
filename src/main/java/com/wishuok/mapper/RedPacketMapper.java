package com.wishuok.mapper;

import com.wishuok.pojo.RedPacket;
import org.apache.ibatis.annotations.Param;

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

    RedPacket selectByPrimariKeyForUpdate(Integer id);

    // 使用 version 而不是 stock 字段判断，是因为防止ABA问题
    // 此处使用 stock 和 version 都行，因为只有对 stock 的 +1 操作
    int decreaseRedPackWithVersion(@Param("id") int id, @Param("version") int version, @Param("stock") int stock);
}