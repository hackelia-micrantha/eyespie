package com.micrantha.bluebell.platform

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import java.io.ByteArrayOutputStream


actual fun ByteArray.toPainter(): Painter {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
    return BitmapPainter(bitmap.asImageBitmap())
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
