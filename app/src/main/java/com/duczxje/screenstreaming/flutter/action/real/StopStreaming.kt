package com.duczxje.screenstreaming.flutter.action.real

import com.duczxje.concurrency.AppCoroutineScope
import com.duczxje.concurrency.launchIO
import com.duczxje.concurrency.switchMain
import com.duczxje.domain.repository.StreamingRepository
import com.duczxje.screenstreaming.flutter.FlutterContainer
import com.duczxje.screenstreaming.flutter.action.FlutterAction
import com.duczxje.screenstreaming.service.StreamingForegroundService
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StopStreaming : FlutterAction, KoinComponent {
    override fun doAction(
        container: FlutterContainer,
        methodCall: MethodCall,
        methodResult: MethodChannel.Result
    ) {
        val context = container.getHostContext()
        try {
            StreamingForegroundService.stop(context)

            methodResult.success(true)
        } catch (err: Exception) {
            methodResult.error(
                "error",
                err.message,
                null
            )
        }
    }
}