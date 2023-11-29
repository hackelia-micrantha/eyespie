package com.micrantha.bluebell.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return with(Image.makeFromEncoded(this)) {
        toComposeImageBitmap()
    }
}
