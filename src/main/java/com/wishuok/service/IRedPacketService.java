package com.wishuok.service;

import com.wishuok.pojo.RedPacket;

public interface IRedPacketService {

    public RedPacket getRedPacket(int id);

    public int decreaseRedPacket(int id);
}
