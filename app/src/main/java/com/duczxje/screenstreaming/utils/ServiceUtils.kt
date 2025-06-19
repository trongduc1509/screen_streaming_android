package com.duczxje.screenstreaming.utils

import android.app.ActivityManager
import android.app.Service
import android.content.Context

fun <T: Service> checkServiceRunning(context: Context, clazz: Class<T>): Boolean {
    val manager = context.getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager
    return manager.getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == clazz.name }
}
