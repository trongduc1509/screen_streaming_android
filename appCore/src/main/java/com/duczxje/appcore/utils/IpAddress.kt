package com.duczxje.appcore.utils

import java.net.Inet4Address
import java.net.NetworkInterface

fun getLocalIpAddress(): String? {
    return try {
        NetworkInterface.getNetworkInterfaces()
            .toList()
            .flatMap { it.inetAddresses.toList() }
            .filterIsInstance<Inet4Address>()
            .firstOrNull { !it.isLoopbackAddress }
            ?.hostAddress
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}