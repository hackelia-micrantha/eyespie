package com.micrantha.skouter.platform.scan

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.asImageBitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.framework.image.MediaImageBuilder
import com.micrantha.bluebell.platform.toByteArray

actual class CameraImage(
    private val data: Image? = null,
    actual val width: Int,
    actual val height: Int,
    val rotation: Int,
    val timestamp: Long
) {

    constructor(bitmap: Bitmap, timestamp: Long) : this(
        data = null,
        width = bitmap.width,
        height = bitmap.height,
        rotation = 0,
        timestamp = timestamp
    ) {
        bitmapBuffer = bitmap
    }

    private lateinit var bitmapBuffer: Bitmap

    actual fun toImageBitmap() = toBitmap().asImageBitmap()

    actual fun toByteArray() = toBitmap().toByteArray()

    fun asMPImage(): MPImage =
        data?.let { MediaImageBuilder(it).build() } ?: BitmapImageBuilder(toBitmap()).build()

    fun toBitmap(): Bitmap {
        if (::bitmapBuffer.isInitialized) return bitmapBuffer

        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            copyPixelsFromBuffer(data!!.planes[0].buffer)
        }

        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())

        bitmapBuffer = Bitmap.createBitmap(
            result,
            0,
            0,
            width,
            height,
            matrix,
            false
        )

        return bitmapBuffer
    }

    actual fun crop(rect: Rect): CameraImage {
        val cropped = Bitmap.createBitmap(
            toBitmap(),
            rect.left.toInt(),
            rect.top.toInt(),
            rect.width.toInt(),
            rect.height.toInt()
        )
        return CameraImage(cropped, timestamp)
    }
}
