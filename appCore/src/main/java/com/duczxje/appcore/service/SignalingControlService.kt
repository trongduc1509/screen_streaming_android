package com.duczxje.appcore.service

import com.duczxje.appcore.socket.TcpSocketManager
import com.duczxje.appcore.socket.UdpSocketManager
import com.duczxje.data.model.ClientInfo
import com.duczxje.data.service.SignalingService

class SignalingControlService(
    private val tcpSocketManager: TcpSocketManager,
    private val udpSocketManager: UdpSocketManager,
) : SignalingService {
    override fun startFrameServer(port: Int, onReady: (ClientInfo) -> Unit) {
        udpSocketManager.startServer(
            port = port,
            onReady = { ip, port ->
                onReady(ClientInfo(ip, port))
            }
        )
    }

    override fun startSignalServer(
        port: Int,
        onReady: (ClientInfo) -> Unit,
        onClientConnected: () -> Unit
    ) {
        tcpSocketManager.startTcpServer(
            port = port,
            onReady = { ip, port ->
                onReady(ClientInfo(ip, port))
            },
            onClientConnected = onClientConnected
        )
    }

    override fun connectToPeer(ip: String, port: Int) {
        tcpSocketManager.connectToHost(ip, port)
    }

    override fun closeFrameServer() {
        udpSocketManager.stopServer()
    }

    override fun closeSignalServer() {
        tcpSocketManager.close()
    }
}