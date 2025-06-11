package com.duczxje.screenstreaming.flutter.action.real

import com.duczxje.concurrency.AppCoroutineScope
import com.duczxje.concurrency.launchIO
import com.duczxje.concurrency.switchMain
import com.duczxje.domain.repository.SignalingRepository
import com.duczxje.screenstreaming.flutter.FlutterContainer
import com.duczxje.screenstreaming.flutter.action.FlutterAction
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StopSignalingServer : FlutterAction, KoinComponent {
    private val appScope : AppCoroutineScope by inject()

    private val signalingControlRepo : SignalingRepository by inject()

    override fun doAction(
        container: FlutterContainer,
        methodCall: MethodCall,
        methodResult: MethodChannel.Result
    ) {
        appScope.launchIO {
            try {
                signalingControlRepo.closeSignalingServer()
                switchMain {
                    methodResult.success(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                switchMain {
                    methodResult.error(
                        "error",
                        e.message,
                        null
                    )
                }
            }
        }
    }
}