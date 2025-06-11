package com.duczxje.appcore.socket

import com.duczxje.appcore.utils.getLocalIpAddress
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UdpSocketManagerV1 : UdpSocketManager {
    private var socket: DatagramSocket? = null

    override fun startServer(port: Int, onReady: (ip: String, port: Int) -> Unit) {
        socket = DatagramSocket(port).apply { broadcast = true }

        onReady(getLocalIpAddress().orEmpty(), port)
    }

    override fun stopServer() {
        socket?.close()
    }

    override fun sendBytes(data: ByteArray, ip: String, port: Int) {
        val packet = DatagramPacket(data, data.size, InetAddress.getByName(ip), port)
        socket?.send(packet)
    }

    override fun receiveBytes(): ByteArray {
        val buffer = ByteArray(65535)
        val packet = DatagramPacket(buffer, buffer.size)
        socket?.receive(packet)
        return packet.data.copyOf(packet.length)
    }
}