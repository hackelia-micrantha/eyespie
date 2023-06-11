package com.micrantha.skouter.platform

import android.media.Image
import com.micrantha.bluebell.platform.toByteArray

actual class CameraImage(
    val image: Image,
    val rotation: Int
) {
    actual val width = image.width
    actual val height = image.height

    actual fun toByteArray() = image.toByteArray()
}
