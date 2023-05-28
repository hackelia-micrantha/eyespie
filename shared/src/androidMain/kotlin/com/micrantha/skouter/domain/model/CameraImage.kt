package com.micrantha.skouter.domain.model

import android.media.Image
import java.nio.ByteBuffer

actual data class CameraImage(
    val image: Image,
    val rotation: Int
) {
    actual fun toByteArray(): ByteArray {
        return image.planes[0].buffer.toByteArray()
    }
}


private fun ByteBuffer.toByteArray(): ByteArray {
    rewind()    // Rewind the buffer to zero
    val data = ByteArray(remaining())
    get(data)   // Copy the buffer into a byte array
    return data // Return the byte array
}
