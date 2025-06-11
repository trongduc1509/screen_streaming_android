package com.duczxje.screenstreaming.flutter

import android.content.Context
import android.content.Intent
import com.duczxje.screenstreaming.flutter.channel.FlutterEventChannel
import com.duczxje.screenstreaming.flutter.channel.FlutterMethodChannel
import io.flutter.embedding.engine.FlutterEngine

interface FlutterContainer {
    fun onNewIntentReceived(intent: Intent)

    fun getHostContext(): Context

    fun getHostFlutterEngine(): FlutterEngine

    fun getHostFlutterMethodChannel(): FlutterMethodChannel

    fun getHostFlutterEventChannel(): FlutterEventChannel

    fun registerFlutterResultReceiver(code: Int, receiver: FlutterResultReceiver)
}