package com.duczxje.screenstreaming.flutter.action

import com.duczxje.screenstreaming.flutter.FlutterContainer
import com.duczxje.screenstreaming.flutter.action.real.StartConnectingToPeer
import com.duczxje.screenstreaming.flutter.action.real.StartFrameStreamingServer
import com.duczxje.screenstreaming.flutter.action.real.StartReceiving
import com.duczxje.screenstreaming.flutter.action.real.StartSignalingServer
import com.duczxje.screenstreaming.flutter.action.real.StartStreaming
import com.duczxje.screenstreaming.flutter.action.real.StopFrameStreamingServer
import com.duczxje.screenstreaming.flutter.action.real.StopReceiving
import com.duczxje.screenstreaming.flutter.action.real.StopSignalingServer
import com.duczxje.screenstreaming.flutter.action.real.StopStreaming
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

object FlutterActionRegistry {
    private val actions = mapOf<String, FlutterActionFactory>(
        "startConnectingToPeer" to factoryOf(StartConnectingToPeer::class.java),
        "startSignalingServer" to factoryOf(StartSignalingServer::class.java),
        "startFrameStreamingServer" to factoryOf(StartFrameStreamingServer::class.java),
        "stopSignalingServer" to factoryOf(StopSignalingServer::class.java),
        "stopFrameStreamingServer" to factoryOf(StopFrameStreamingServer::class.java),
        "startStreaming" to factoryOf(StartStreaming::class.java),
        "stopStreaming" to factoryOf(StopStreaming::class.java),
        "startReceiving" to factoryOf(StartReceiving::class.java),
        "stopReceiving" to factoryOf(StopReceiving::class.java),
    )

    private fun <T : FlutterAction> factoryOf(clazz: Class<T>): FlutterActionFactory {
        return object : FlutterActionFactory {
            override fun create(): FlutterAction {
                return clazz.getDeclaredConstructor().newInstance()
            }
        }
    }

    fun invoke(
        container: FlutterContainer,
        methodCall: MethodCall,
        methodResult: MethodChannel.Result
    ): Boolean {
        return actions[methodCall.method]?.create()
            ?.doAction(container, methodCall, methodResult)
            ?.let { true } ?: false
    }
}