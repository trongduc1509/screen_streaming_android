package com.duczxje.screenstreaming.flutter.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.duczxje.screenstreaming.flutter.FlutterContainer
import com.duczxje.screenstreaming.flutter.FlutterResultReceiver
import com.duczxje.screenstreaming.flutter.channel.FlutterEventChannel
import com.duczxje.screenstreaming.flutter.channel.FlutterFrameEventChannel
import com.duczxje.screenstreaming.flutter.channel.FlutterMainChannel
import com.duczxje.screenstreaming.flutter.channel.FlutterMethodChannel
import com.duczxje.screenstreaming.flutter.engine.mainFlutterEngine
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.engine.FlutterEngine

class FlutterContainerFragment : FlutterFragment(), FlutterContainer {
    companion object {
        fun newInstance(args: Bundle? = null): FlutterFragment {
            return CachedEngineFragmentBuilder(FlutterContainerFragment::class.java, mainFlutterEngine)
                .renderMode(RenderMode.texture)
                .build<FlutterContainerFragment>()
                .apply {
                    if (args != null) arguments?.putAll(args)
                }
        }
    }

    private val mainChannel: FlutterMethodChannel by lazy {
        FlutterMainChannel(this)
    }

    private val frameEventChannel: FlutterEventChannel by lazy {
        FlutterFrameEventChannel(this)
    }

    private val resultReceiverMap = mutableMapOf<Int, FlutterResultReceiver>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        mainChannel.initialize()
        frameEventChannel.initialize()
    }

    override fun onFlutterUiDisplayed() {
        super.onFlutterUiDisplayed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        processFlutterResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        resultReceiverMap.clear()
        super.onDestroy()
    }

    override fun onNewIntentReceived(intent: Intent) {
        TODO("Not yet implemented")
    }

    override fun getHostContext(): Context {
        return context
    }

    override fun getHostFlutterEngine(): FlutterEngine {
        return flutterEngine ?: throw RuntimeException("FlutterEngine is not ready")
    }

    override fun getHostFlutterMethodChannel(): FlutterMethodChannel {
        return mainChannel
    }

    override fun getHostFlutterEventChannel(): FlutterEventChannel {
        return frameEventChannel
    }

    override fun registerFlutterResultReceiver(code: Int, receiver: FlutterResultReceiver) {
        synchronized(resultReceiverMap) {
            resultReceiverMap[code] = receiver
        }
    }

    private fun processFlutterResult(code: Int, resultCode: Int, resultData: Any?) {
        synchronized(resultReceiverMap) {
            resultReceiverMap[code]?.onResult(resultCode, resultData)
            resultReceiverMap.remove(code)
        }
    }
}