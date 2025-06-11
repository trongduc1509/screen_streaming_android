package com.duczxje.appcore.capture

import android.content.Intent

interface CaptureManager {
    fun initialize(resultCode: Int, data: Intent, config: CaptureConfig)

    fun startCapturing(onFrameCaptured: (ByteArray) -> Unit)

    fun stopCapturing()
}
