package com.duczxje.screenstreaming.flutter

import io.flutter.plugin.common.MethodChannel

abstract class FlutterResultReceiver(
    protected val channelResult: MethodChannel.Result
) {
    abstract fun onResult(data: Any?)
}