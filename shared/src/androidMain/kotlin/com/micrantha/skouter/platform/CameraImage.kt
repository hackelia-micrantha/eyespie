package com.micrantha.skouter.platform

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.micrantha.bluebell.platform.toByteArray
import kotlin.math.max

actual class CameraImage(
    val bitmap: Bitmap,
) {
    actual val width = bitmap.width
    actual val height = bitmap.height

    actual fun toImageBitmap() = bitmap.asImageBitmap()

    actual fun toByteArray() = bitmap.toByteArray()

    fun scale(width: Int, height: Int): CameraImage {
        val scaleFactor =
            max(this.width * 1f / width, this.height * 1f / height)
        val scaleWidth = (this.width * scaleFactor).toInt()
        val scaleHeight = (this.height * scaleFactor).toInt()

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, false)

        return CameraImage(scaledBitmap)
    }

}
