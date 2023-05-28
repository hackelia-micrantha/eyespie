package com.micrantha.bluebell.platform

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

actual fun ByteArray.toPainter(): Painter {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
    return bitmap.toPainter()
}

fun Bitmap.toPainter(): Painter {
    return BitmapPainter(asImageBitmap())
}

