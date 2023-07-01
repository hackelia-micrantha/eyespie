package com.micrantha.skouter.platform

import androidx.compose.ui.graphics.ImageBitmap
import platform.UIKit.UIImage

actual data class CameraImage(
    val data: UIImage
) {
    actual val width: Int = 0
    actual val height: Int = 0

    actual fun toByteArray() = ByteArray(0)

    actual fun toImageBitmap(): ImageBitmap {
        TODO("not implemented yet")
    }

    actual fun release() {
    }
}
