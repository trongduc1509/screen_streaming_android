package com.duczxje.appcore.socket

interface TcpSocketManager {
    fun startTcpServer(
        port: Int,
        onReady: (ip: String, port: Int) -> Unit,
        onClientConnected: () -> Unit
    )

    fun connectToHost(ip: String, port: Int)

    fun sendBytes(data: ByteArray)

    fun receiveBytes(): ByteArray

    fun close()
}