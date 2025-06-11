package com.duczxje.screenstreaming.flutter.channel

import com.duczxje.screenstreaming.flutter.FlutterContainer
import io.flutter.plugin.common.EventChannel

private const val FRAME_EVENT_CHANNEL_NAME = "com.duczxje.io/channel/frame_event"

class FlutterFrameEventChannel(
    private val container: FlutterContainer,
) : FlutterEventChannel {
    private val mainEventChannel by lazy {
        EventChannel(
            container.getHostFlutterEngine().dartExecutor.binaryMessenger,
            FRAME_EVENT_CHANNEL_NAME
        )
    }

    private var frameEventSink: EventChannel.EventSink? = null

    override fun initialize() {
        mainEventChannel.setStreamHandler(
            object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                    frameEventSink = events
                }

                override fun onCancel(arguments: Any?) {
                    frameEventSink = null
                }
            }
        )
    }

    override fun dispose() {
        mainEventChannel.setStreamHandler(null)
    }

    override fun sendEvent(event: Any) {
        frameEventSink?.success(event)
    }
}