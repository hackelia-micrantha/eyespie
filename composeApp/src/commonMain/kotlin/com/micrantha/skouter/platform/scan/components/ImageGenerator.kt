package com.micrantha.skouter.platform.scan.components

import com.micrantha.skouter.platform.scan.CameraImage

interface ImageGenerator<in T> {
    fun generate(from: T): Result<CameraImage>
}