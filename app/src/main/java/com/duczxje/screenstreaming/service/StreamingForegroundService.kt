package com.duczxje.screenstreaming.service

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.duczxje.appcore.capture.CaptureConfig
import com.duczxje.appcore.capture.CaptureManager
import com.duczxje.appcore.capture.CaptureManagerV1
import com.duczxje.concurrency.AppCoroutineScope
import com.duczxje.concurrency.launchIO
import com.duczxje.domain.repository.StreamingRepository
import com.duczxje.screenstreaming.R
import com.duczxje.screenstreaming.utils.checkServiceRunning
import org.koin.android.ext.android.inject

class StreamingForegroundService : Service() {
    private val captureManager : CaptureManager by inject()
    private val streamingControlRepo : StreamingRepository by inject()
    private val appCoroutineScope : AppCoroutineScope by inject()

    companion object {
        const val ACTION_START = "com.duczxje.screenstreaming.START"
        const val ACTION_STOP = "com.duczxje.screenstreaming.STOP"
        private const val CHANNEL_ID = "screen_stream"

        @RequiresApi(Build.VERSION_CODES.O)
        fun start(
            context: Context,
            ip: String,
            port: Int,
            width: Int,
            height: Int,
            density: Int,
            resultCode: Int,
            resultData: Intent,
        ) {
            if (!isServiceRunning(context)) {
                val intent = Intent(context, StreamingForegroundService::class.java)
                intent.action = ACTION_START
                intent.putExtra("result_code", resultCode)
                intent.putExtra("result_data", resultData)
                intent.putExtra("viewer_ip", ip)
                intent.putExtra("viewer_port", port)
                intent.putExtra("width", width)
                intent.putExtra("height", height)
                intent.putExtra("density", density)
                ContextCompat.startForegroundService(context, intent)
            }
        }

        fun stop(context: Context) {
            if (isServiceRunning(context)) {
                val intent = Intent(context, StreamingForegroundService::class.java)
                intent.action = ACTION_STOP
                context.startService(intent)
            }
        }

        private fun isServiceRunning(context: Context): Boolean {
            return checkServiceRunning(context, StreamingForegroundService::class.java)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> handleStart(intent)
            ACTION_STOP -> handleStop()
        }
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleStart(intent: Intent) {
        startForeground(1, buildNotification())

        val resultCode = intent.getIntExtra("result_code", Activity.RESULT_CANCELED)
        val resultData = intent.getParcelableExtra<Intent>("result_data") ?: return

        val width = intent.getIntExtra("width", 0)
        val height = intent.getIntExtra("height", 0)
        val density = intent.getIntExtra("density", 0)
        val ip = intent.getStringExtra("viewer_ip") ?: return
        val port = intent.getIntExtra("viewer_port", 0)
        if (port == 0) return

        val config = CaptureConfig(applicationContext, width, height, density)

        captureManager.let {
            captureManager.initialize(resultCode, resultData, config)

            appCoroutineScope.launchIO {
                streamingControlRepo.startStreaming(ip, port)
            }
        }
    }

    private fun handleStop() {
        streamingControlRepo.stopStreaming()

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildNotification() : Notification {
        createNotificationChannelIfNeeded()

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Screen streaming in progress")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelIfNeeded() {
        val notiManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (notiManager.getNotificationChannel(CHANNEL_ID) == null) {
            val notiChannel = NotificationChannel(
                CHANNEL_ID,
                "Screen Streaming",
                NotificationManager.IMPORTANCE_LOW,
            )
            notiManager.createNotificationChannel(notiChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}