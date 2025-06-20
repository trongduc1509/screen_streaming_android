package com.duczxje.screenstreaming.flutter.action.real

import com.duczxje.concurrency.AppCoroutineScope
import com.duczxje.concurrency.launchMain
import com.duczxje.domain.repository.StreamingRepository
import com.duczxje.screenstreaming.flutter.FlutterContainer
import com.duczxje.screenstreaming.flutter.action.FlutterAction
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StartReceiving : FlutterAction, KoinComponent {
    private val appCoroutineScope: AppCoroutineScope by inject()

    private val streamingControlRepo: StreamingRepository by inject()

    override fun doAction(
        container: FlutterContainer, methodCall: MethodCall, methodResult: MethodChannel.Result
    ) {
        try {
            streamingControlRepo.startReceiving(onFrameReceived = { frameEntity ->
                appCoroutineScope.launchMain {
                    container.getHostFlutterEventChannel().sendEvent(frameEntity.payload)
                }
            })
            methodResult.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            methodResult.error(
                "error", e.message, null
            )
        }
    }
}