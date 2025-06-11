package com.duczxje.appcore.repository

import com.duczxje.appcore.utils.toEntity
import com.duczxje.data.service.StreamingService
import com.duczxje.domain.entity.FrameEntity
import com.duczxje.domain.repository.StreamingRepository

class StreamingControlRepository(
    private val streamingService: StreamingService
) : StreamingRepository {
    override fun startStreaming(ip: String, port: Int) {
        streamingService.startStreaming(ip, port)
    }

    override fun stopStreaming() {
        streamingService.stopStreaming()
    }

    override fun startReceiving(onFrameReceived: (FrameEntity) -> Unit) {
        streamingService.startReceiving {
            onFrameReceived(it.toEntity())
        }
    }

    override fun stopReceiving() {
        streamingService.stopReceiving()
    }
}