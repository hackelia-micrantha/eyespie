package com.micrantha.skouter.platform

import android.graphics.Bitmap
import android.graphics.Matrix
import com.micrantha.bluebell.platform.toByteArray
import com.seiko.imageloader.asImageBitmap
import kotlin.math.max

actual class CameraImage(
    val bitmap: Bitmap,
    val rotation: Int
) {
    actual val width = bitmap.width
    actual val height = bitmap.height

    actual fun rotate(): CameraImage {
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
        return CameraImage(rotatedBitmap, 0)
    }

    actual fun toImageBitmap() = bitmap.asImageBitmap()

    actual fun toByteArray() = bitmap.toByteArray()

    fun scale(width: Int, height: Int): CameraImage {
        val scaleFactor =
            max(this.width * 1f / width, this.height * 1f / height)
        val scaleWidth = (this.width * scaleFactor).toInt()
        val scaleHeight = (this.height * scaleFactor).toInt()

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, false)

        return CameraImage(scaledBitmap, rotation)
    }

}
