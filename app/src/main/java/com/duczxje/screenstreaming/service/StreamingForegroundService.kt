package com.duczxje.screenstreaming.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class StreamingForegroundService : Service() {
    companion object {
        const val ACTION_START = "com.duczxje.screenstreaming.START"
        const val ACTION_STOP = "com.duczxje.screenstreaming.STOP"
        private const val CHANNEL_ID = "screen_stream"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> handleStart(intent)
            ACTION_STOP -> handleStop()
        }
        return START_STICKY
    }

    private fun handleStart(intent: Intent) {

    }

    private fun handleStop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}