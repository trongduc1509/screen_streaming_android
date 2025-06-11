package com.duczxje.data.service

import com.duczxje.data.model.FramePacket

interface StreamingService {
    fun startStreaming(ip: String, port: Int)

    fun stopStreaming()

    fun startReceiving(onFrameReceived: (FramePacket) -> Unit)

    fun stopReceiving()
}