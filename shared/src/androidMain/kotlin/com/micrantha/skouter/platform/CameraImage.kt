package com.micrantha.skouter.platform

import android.media.Image
import java.nio.ByteBuffer

actual class CameraImage(
    val image: Image,
    private val data: ByteArray,
    val rotation: Int
) {
    actual fun toByteArray() = data
}

fun Image.toByteArray(): ByteArray {
    return planes[0].buffer.toByteArray()
}

private fun ByteBuffer.toByteArray(): ByteArray {
    rewind()    // Rewind the buffer to zero
    val data = ByteArray(remaining())
    get(data)   // Copy the buffer into a byte array
    return data // Return the byte array
}
