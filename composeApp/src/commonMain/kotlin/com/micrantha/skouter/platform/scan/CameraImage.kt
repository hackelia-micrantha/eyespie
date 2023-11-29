package com.micrantha.skouter.platform.scan

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap

expect class CameraImage {

    val width: Int
    val height: Int

    fun toByteArray(): ByteArray

    fun toImageBitmap(): ImageBitmap

    fun crop(rect: Rect): CameraImage
}
