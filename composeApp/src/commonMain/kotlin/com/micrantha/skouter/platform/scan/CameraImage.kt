package com.micrantha.skouter.platform.scan

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap

expect class CameraImage

expect val CameraImage.width: Int
expect val CameraImage.height: Int

expect fun CameraImage.toByteArray(): ByteArray

expect fun CameraImage.toImageBitmap(): ImageBitmap

expect fun CameraImage.crop(rect: Rect): CameraImage
