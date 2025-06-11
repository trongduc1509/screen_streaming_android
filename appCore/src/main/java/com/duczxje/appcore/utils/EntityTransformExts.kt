package com.duczxje.appcore.utils

import com.duczxje.data.model.ClientInfo
import com.duczxje.data.model.FramePacket
import com.duczxje.domain.entity.FrameEntity
import com.duczxje.domain.entity.PeerEntity
import com.duczxje.domain.entity.PeerRole

fun ClientInfo.toEntity(): PeerEntity {
    return PeerEntity(
        id = "RECEIVING_PEER",
        role = PeerRole.RECEIVER,
        ip = ip,
        port = port
    )
}

fun FramePacket.toEntity(): FrameEntity {
    return FrameEntity(
        payload = payload
    )
}