package com.duczxje.screenstreaming.flutter.action

import com.duczxje.screenstreaming.flutter.FlutterContainer
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

interface FlutterAction {
    fun doAction(
        container: FlutterContainer,
        methodCall: MethodCall,
        methodResult: MethodChannel.Result
    )
}