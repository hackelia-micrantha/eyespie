package com.micrantha.skouter.platform

import androidx.compose.ui.graphics.ImageBitmap

expect class CameraImage {

    val width: Int
    val height: Int

    fun rotate(): CameraImage

    fun toByteArray(): ByteArray

    fun toImageBitmap(): ImageBitmap
}
