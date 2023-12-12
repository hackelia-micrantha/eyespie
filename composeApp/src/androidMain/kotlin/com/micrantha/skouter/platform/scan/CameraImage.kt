package com.micrantha.skouter.platform.scan

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.asImageBitmap
import com.micrantha.bluebell.platform.toByteArray

actual class CameraImage(
    val bitmap: Bitmap,
    val rotation: Int,
    val timestamp: Long
)

actual val CameraImage.width: Int
    get() = bitmap.width
actual val CameraImage.height: Int
    get() = bitmap.height

actual fun CameraImage.toImageBitmap() = bitmap.asImageBitmap()

actual fun CameraImage.toByteArray() = bitmap.toByteArray()

actual fun CameraImage.crop(rect: Rect): CameraImage {
    val cropped = Bitmap.createBitmap(
        bitmap,
        rect.left.toInt(),
        rect.top.toInt(),
        rect.width.toInt(),
        rect.height.toInt()
    )
    return CameraImage(cropped, rotation, timestamp)
}