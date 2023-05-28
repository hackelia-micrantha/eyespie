package com.micrantha.bluebell.platform

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual fun ByteArray.toPainter(): Painter {
    return with(Image.makeFromEncoded(this)) {
        BitmapPainter(toComposeImageBitmap())
    }
}
