package com.duczxje.screenstreaming.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class ConnectionForegroundService: Service() {
    private val binder = LocalBinder()

    private var isConnected = false
    private var peerIp = ""
    private var connectedStartTime: Long = 0

    companion object {
        const val CHANNEL_ID = "com.duczxje.connnection_status_service"
        const val NOTIFICATION_ID = 1
    }

    inner class LocalBinder : Binder() {
        fun getService(): ConnectionForegroundService = this@ConnectionForegroundService
    }

    data class ConnectionInfo(
        val isConnected: Boolean,
        val peerInfo: String,
        val connectedTime: Long,
    )

    fun getConnectionInfo(): ConnectionInfo {
        return ConnectionInfo(
            isConnected = isConnected,
            peerInfo = peerIp,
            connectedTime = System.currentTimeMillis() - connectedStartTime,
        )
    }

    override fun onBind(intent: Intent?): IBinder = binder
}