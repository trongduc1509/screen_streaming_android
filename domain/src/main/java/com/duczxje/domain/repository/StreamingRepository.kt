package com.duczxje.domain.repository

import com.duczxje.domain.entity.FrameEntity

interface StreamingRepository {
    fun startStreaming(ip: String, port: Int)

    fun stopStreaming()

    fun startReceiving(onFrameReceived: (FrameEntity) -> Unit)

    fun stopReceiving()
}