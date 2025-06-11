package com.duczxje.appcore.capture

import android.content.Context

data class CaptureConfig(
    val context: Context,
    val width: Int,
    val height: Int,
    val density: Int,
)