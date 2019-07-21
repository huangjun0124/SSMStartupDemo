package com.wishuok.service;

public interface IUserRedPacketService {
    int grabRedPacket(int redPacketId, int userId);

    int grabRedPacketWithVersion(int redPacketId, int userId);
}
