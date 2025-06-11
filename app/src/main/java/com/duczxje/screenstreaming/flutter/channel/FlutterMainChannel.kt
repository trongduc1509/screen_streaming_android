package com.duczxje.screenstreaming.flutter.channel

import com.duczxje.screenstreaming.flutter.FlutterContainer
import com.duczxje.screenstreaming.flutter.action.FlutterActionRegistry
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

private const val MAIN_CHANNEL_NAME = "com.duczxje.io/channel/main"

class FlutterMainChannel(
    private val container: FlutterContainer,
) : FlutterMethodChannel {
    private val mainChannel by lazy {
        MethodChannel(
            container.getHostFlutterEngine().dartExecutor.binaryMessenger,
            MAIN_CHANNEL_NAME
        )
    }

    override fun initialize() {
        mainChannel.setMethodCallHandler(::onMainMethodCall)
    }

    override fun dispose() {
        mainChannel.setMethodCallHandler(null)
    }

    private fun onMainMethodCall(method: MethodCall, result: MethodChannel.Result) {
        FlutterActionRegistry.invoke(container, method, result)
    }
}