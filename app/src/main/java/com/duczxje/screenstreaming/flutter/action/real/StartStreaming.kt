package com.duczxje.screenstreaming.flutter.action.real

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.duczxje.screenstreaming.flutter.FlutterContainer
import com.duczxje.screenstreaming.flutter.FlutterResultReceiver
import com.duczxje.screenstreaming.flutter.action.FlutterAction
import com.duczxje.screenstreaming.flutter.fragment.FlutterContainerFragment
import com.duczxje.screenstreaming.service.StreamingForegroundService
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.koin.core.component.KoinComponent

class StartStreaming : FlutterAction, KoinComponent {
    private val requestCode = 0x1234

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doAction(
        container: FlutterContainer,
        methodCall: MethodCall,
        methodResult: MethodChannel.Result
    ) {
        val arguments = methodCall.arguments as Map<*, *>
        val ip = arguments["ip"] as? String
        val port = arguments["port"] as? Int

        if (ip != null && port != null) {
            val context = container.getHostContext()

            (context as? Activity)?.let { activity ->
                val displayMetrics = DisplayMetrics().also {
                    activity.windowManager.getDefaultDisplay().getMetrics(it)
                }

                requestMediaProjectionPermission(context, container)

                container.registerFlutterResultReceiver(
                    requestCode,
                    object : FlutterResultReceiver(methodResult) {
                        override fun onResult(resultCode: Int, resultData: Any?) {
                            when {
                                resultData is Intent -> {
                                    try {
                                        StreamingForegroundService.start(
                                            context = context,
                                            ip = ip,
                                            port = port,
                                            width = displayMetrics.widthPixels,
                                            height = displayMetrics.heightPixels,
                                            density = displayMetrics.densityDpi,
                                            resultCode = resultCode,
                                            resultData = resultData,
                                        )

                                        channelResult.success(true)
                                    } catch (err: Exception) {
                                        channelResult.error(
                                            "error",
                                            err.message,
                                            null
                                        )
                                    }
                                }

                                else -> {
                                    methodResult.error(
                                        "error",
                                        "Request media projection permission failed",
                                        null
                                    )
                                }
                            }
                        }
                    }
                )
            }
        } else {
            methodResult.error(
                "error",
                "ip or port is null",
                null
            )
        }
    }

    private fun requestMediaProjectionPermission(context: Context, container: FlutterContainer) {
        val manager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        (container as? FlutterContainerFragment)?.startActivityForResult(manager.createScreenCaptureIntent(), requestCode)
    }
}