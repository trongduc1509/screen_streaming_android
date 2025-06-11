package com.duczxje.data.service

import com.duczxje.data.model.ClientInfo

interface SignalingService {
    fun startFrameServer(port: Int, onReady: (ClientInfo) -> Unit)

    fun startSignalServer(
        port: Int,
        onReady: (ClientInfo) -> Unit,
        onClientConnected: () -> Unit
    )

    fun connectToPeer(ip: String, port: Int)

    fun closeFrameServer()

    fun closeSignalServer()
}