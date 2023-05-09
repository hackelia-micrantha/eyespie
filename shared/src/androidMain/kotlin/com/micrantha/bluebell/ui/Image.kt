package com.micrantha.bluebell.ui

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

actual fun ByteArray.toPainter(): Painter {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
    return BitmapPainter(bitmap.asImageBitmap())
}
