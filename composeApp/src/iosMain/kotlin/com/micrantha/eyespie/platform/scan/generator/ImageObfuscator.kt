package com.micrantha.eyespie.platform.scan.generator

import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.ImageGenerator

actual class ImageObfuscator : ImageGenerator<CameraImage> {
    actual override suspend fun generate(from: CameraImage): Result<CameraImage> {
        TODO("Not yet implemented")
    }
}
