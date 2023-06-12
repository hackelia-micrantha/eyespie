package com.micrantha.skouter.platform

import android.graphics.Bitmap
import android.graphics.Matrix
import com.micrantha.bluebell.platform.toByteArray
import com.seiko.imageloader.asImageBitmap

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
}
