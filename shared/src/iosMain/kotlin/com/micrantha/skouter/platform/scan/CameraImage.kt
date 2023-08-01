package com.micrantha.skouter.platform.scan

import androidx.compose.ui.graphics.ImageBitmap
import com.micrantha.bluebell.platform.toImageBitmap

actual data class CameraImage(
    val data: ByteArray,
    actual val width: Int,
    actual val height: Int
) {

    actual fun toByteArray() = data

    actual fun toImageBitmap(): ImageBitmap = data.toImageBitmap()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CameraImage) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int = data.contentHashCode()
}
