package com.duczxje.appcore.socket

interface UdpSocketManager {
    fun startServer(port: Int, onReady: (ip: String, port: Int) -> Unit)

    fun stopServer()

    fun sendBytes(data: ByteArray, ip: String, port: Int)

    fun receiveBytes(): ByteArray
}