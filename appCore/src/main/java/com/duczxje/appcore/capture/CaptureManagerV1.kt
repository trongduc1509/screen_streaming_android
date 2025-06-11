package com.duczxje.appcore.capture

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import java.io.ByteArrayOutputStream

class CaptureManagerV1 : CaptureManager {
    private lateinit var mediaProjection: MediaProjection
    private lateinit var config: CaptureConfig
    private lateinit var imageReader: ImageReader
    private lateinit var virtualDisplay: VirtualDisplay
    private lateinit var captureHandlerThread: HandlerThread
    private lateinit var captureHandler: Handler

    @Volatile
    private var isCapturing: Boolean = false

    override fun initialize(resultCode: Int, data: Intent, config: CaptureConfig) {
        this.config = config
        val manager = config.context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjection = manager.getMediaProjection(resultCode, data)

        mediaProjection.registerCallback(
            object : MediaProjection.Callback() {
                override fun onStop() {
                    super.onStop()
                    stopCapturing()
                }
            },
            Handler(Looper.getMainLooper())
        )

        imageReader = ImageReader.newInstance(config.width, config.height, PixelFormat.RGBA_8888, 2)

        virtualDisplay = mediaProjection.createVirtualDisplay(
            "duczxje.ScreenSharing",
            config.width, config.height, config.density,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.surface, null, null
        )

        captureHandlerThread = HandlerThread("CaptureThread").also { it.start() }
        captureHandler = Handler(captureHandlerThread.looper)
    }

    override fun startCapturing(onFrameCaptured: (ByteArray) -> Unit) {
        if (isCapturing) return

        isCapturing = true

        imageReader.setOnImageAvailableListener({ reader ->
                if (!isCapturing) return@setOnImageAvailableListener

                val image = reader.acquireLatestImage() ?: return@setOnImageAvailableListener

                try {
                    val bytes = convertImageToBytes(image)

                    onFrameCaptured(bytes)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    image.close()
                }
            },
            captureHandler
        )
    }

    override fun stopCapturing() {
        if (!isCapturing) return

        isCapturing = false
        imageReader.setOnImageAvailableListener(null, null)
        imageReader.close()
        virtualDisplay.release()
        captureHandlerThread.quitSafely()
        mediaProjection.stop()
    }

    private fun convertImageToBytes(image: Image): ByteArray {
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * image.width

        val bitmap = Bitmap.createBitmap(
            image.width + rowPadding / pixelStride,
            image.height,
            Bitmap.Config.ARGB_8888,
        )
        bitmap.copyPixelsFromBuffer(buffer)

        val croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, image.width, image.height)

        val output = ByteArrayOutputStream()
        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, output)
        return output.toByteArray()
    }
}