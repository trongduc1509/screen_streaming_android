package com.duczxje.domain.repository

import com.duczxje.domain.entity.PeerEntity

interface SignalingRepository {
    fun startSignalingServer(
        port: Int,
        onReady: (PeerEntity) -> Unit,
        onClientConnected: () -> Unit
    )

    fun startFrameStreamingServer(
        port: Int,
        onReady: (PeerEntity) -> Unit
    )

    fun connectToPeer(ip: String, port: Int)

    fun closeFrameStreamingServer()

    fun closeSignalingServer()
}