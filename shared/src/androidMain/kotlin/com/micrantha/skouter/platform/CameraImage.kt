package com.micrantha.skouter.platform

import android.graphics.Bitmap
import com.micrantha.bluebell.platform.toByteArray
import com.seiko.imageloader.asImageBitmap
import kotlinx.coroutines.sync.Mutex
import kotlin.math.max

actual class CameraImage(
    val mutex: Mutex,
    val bitmap: Bitmap
) {
    actual val width = bitmap.width
    actual val height = bitmap.height

    actual fun release() {
        mutex.unlock()
    }

    actual fun toImageBitmap() = bitmap.asImageBitmap()

    actual fun toByteArray() = bitmap.toByteArray()

    fun scale(width: Int, height: Int): CameraImage {
        val scaleFactor =
            max(this.width * 1f / width, this.height * 1f / height)
        val scaleWidth = (this.width * scaleFactor).toInt()
        val scaleHeight = (this.height * scaleFactor).toInt()

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, false)

        return CameraImage(mutex, scaledBitmap)
    }

}
