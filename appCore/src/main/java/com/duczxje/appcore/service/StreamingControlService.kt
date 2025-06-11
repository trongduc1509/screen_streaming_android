package com.duczxje.appcore.service

import com.duczxje.appcore.capture.CaptureManager
import com.duczxje.appcore.socket.UdpSocketManager
import com.duczxje.data.model.FrameChunkPacket
import com.duczxje.data.model.FramePacket
import com.duczxje.data.model.encodeToChunks
import com.duczxje.data.service.StreamingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StreamingControlService(
    private val captureManager: CaptureManager,
    private val udpSocketManager: UdpSocketManager,
) : StreamingService {
    private var streamingJob: Job? = null
    private var receivingJob: Job? = null

    override fun startStreaming(ip: String, port: Int) {
        captureManager.startCapturing { frame ->
            streamingJob = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val framePacket = FramePacket(frame)

                    val chunkedPackets = framePacket.encodeToChunks()

                    chunkedPackets.forEachIndexed { index, bytes ->
                        udpSocketManager.sendBytes(bytes, ip, port)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun stopStreaming() {
        udpSocketManager.stopServer()
        streamingJob?.cancel()
        streamingJob = null
    }

    override fun startReceiving(onFrameReceived: (FramePacket) -> Unit) {
        val chunkBuffer = mutableMapOf<Long, MutableMap<Int, ByteArray>>()

        receivingJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    val bytes = udpSocketManager.receiveBytes()

                    val chunkedPacket = FrameChunkPacket.decode(bytes) ?: continue
                    val frameId = chunkedPacket.frameId
                    val chunkIndex = chunkedPacket.chunkIndex
                    val totalChunks = chunkedPacket.totalChunks

                    val frameChunks = chunkBuffer.getOrPut(frameId) { mutableMapOf() }
                    frameChunks[chunkIndex] = chunkedPacket.payload

                    if (totalChunks == frameChunks.size) {
                        val fullData = ByteArray(frameChunks.values.sumOf { it.size })
                        var offet = 0
                        for (i in 0 until totalChunks) {
                            val part = frameChunks[i] ?: continue
                            System.arraycopy(part, 0, fullData, offet, part.size)
                            offet += part.size
                        }

                        chunkBuffer.remove(frameId)

                        val framePacket = FramePacket.decode(fullData)
                        onFrameReceived(framePacket)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }
            }
        }
    }

    override fun stopReceiving() {
        receivingJob?.cancel()
        receivingJob = null
    }
}