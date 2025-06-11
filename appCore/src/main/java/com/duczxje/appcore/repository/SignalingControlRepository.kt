package com.duczxje.appcore.repository

import com.duczxje.data.service.SignalingService
import com.duczxje.domain.entity.PeerEntity
import com.duczxje.domain.repository.SignalingRepository
import com.duczxje.appcore.utils.toEntity

class SignalingControlRepository(
    private val signalingService: SignalingService
) : SignalingRepository {
    override fun startSignalingServer(
        port: Int,
        onReady: (PeerEntity) -> Unit,
        onClientConnected: () -> Unit
    ) {
        signalingService.startSignalServer(
            port,
            onReady = {
                onReady(it.toEntity())
            },
            onClientConnected = onClientConnected
        )
    }

    override fun startFrameStreamingServer(port: Int, onReady: (PeerEntity) -> Unit) {
        signalingService.startFrameServer(
            port,
            onReady = {
                onReady(it.toEntity())
            }
        )
    }

    override fun connectToPeer(ip: String, port: Int) {
        signalingService.connectToPeer(ip, port)
    }

    override fun closeFrameStreamingServer() {
        signalingService.closeFrameServer()
    }

    override fun closeSignalingServer() {
        signalingService.closeSignalServer()
    }
}