package com.duczxje.screenstreaming.flutter.channel

interface FlutterChannel {
    fun initialize()

    fun dispose()
}

interface FlutterMethodChannel : FlutterChannel

interface FlutterEventChannel : FlutterChannel {
    fun sendEvent(event: Any)
}