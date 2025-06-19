package com.duczxje.appcore.socket

import com.duczxje.appcore.utils.getLocalIpAddress
import com.duczxje.concurrency.AppCoroutineScope
import com.duczxje.concurrency.launchIO
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

class TcpSocketManagerV1(
    private val appCoroutineScope: AppCoroutineScope
) : TcpSocketManager {
    private var socket: Socket? = null

    private var server: ServerSocket? = null

    private var serverJob: Job? = null

    private var inputStream: DataInputStream? = null

    private var outputStream: DataOutputStream? = null

    override fun startTcpServer(
        port: Int,
        onReady: (ip: String, port: Int) -> Unit,
        onClientConnected: () -> Unit
    ) {
        close()

        server = ServerSocket(port)

        onReady(getLocalIpAddress().orEmpty(), port)

        serverJob = appCoroutineScope.launchIO {
            while(isActive) {
                try {
                    listenForClientConnection { ip, port ->
                        onClientConnected()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    close()
                    break
                }
            }
        }
    }

    override fun connectToHost(ip: String, port: Int) {
        socket = Socket(ip, port)

        setupClientIO()
    }

    override fun sendBytes(data: ByteArray) {
        outputStream?.writeInt(data.size)
        outputStream?.write(data)
        outputStream?.flush()
    }

    override fun receiveBytes(): ByteArray {
        val length = inputStream?.readInt() ?: throw IOException("Invalid Input-stream")
        val buffer = ByteArray(length)
        inputStream?.readFully(buffer)
        return buffer
    }

    override fun close() {
        try {
            closeClientIO()
            socket?.close()
            server?.close()
            closeServerJob()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun listenForClientConnection(
        onClientConnected: (ip: String, port: Int) -> Unit,
    ) {
        val client = server?.accept() ?: return

        onClientConnected(client.inetAddress.hostAddress.orEmpty(), client.port)

        client.close()
    }

    private fun setupClientIO() {
        if (socket == null) return
        inputStream = DataInputStream(socket?.getInputStream())
        outputStream = DataOutputStream(socket?.getOutputStream())
    }

    private fun closeClientIO() {
        inputStream?.close()
        outputStream?.close()
    }

    private fun closeServerJob() {
        serverJob?.cancel()
        serverJob = null
    }
}