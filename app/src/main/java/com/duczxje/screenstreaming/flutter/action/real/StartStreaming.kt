package com.duczxje.screenstreaming.flutter.action.real

import com.duczxje.concurrency.AppCoroutineScope
import com.duczxje.concurrency.launchIO
import com.duczxje.concurrency.switchMain
import com.duczxje.domain.repository.StreamingRepository
import com.duczxje.screenstreaming.flutter.FlutterContainer
import com.duczxje.screenstreaming.flutter.action.FlutterAction
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StartStreaming : FlutterAction, KoinComponent {
    private val appScope : AppCoroutineScope by inject()

    private val streamingControlRepo: StreamingRepository by inject()

    override fun doAction(
        container: FlutterContainer,
        methodCall: MethodCall,
        methodResult: MethodChannel.Result
    ) {
        val arguments = methodCall.arguments as Map<*, *>
        val ip = arguments["ip"] as? String
        val port = arguments["port"] as? Int

        if (ip != null && port != null) {
            appScope.launchIO {
                try {
                    streamingControlRepo.startStreaming(
                        ip = ip,
                        port = port
                    )
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
}