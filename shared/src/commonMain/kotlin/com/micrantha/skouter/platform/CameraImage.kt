package com.micrantha.skouter.platform

expect class CameraImage {

    val width: Int
    val height: Int

    fun toByteArray(): ByteArray
}
