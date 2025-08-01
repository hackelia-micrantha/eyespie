package com.micrantha.eyespie.platform.scan.generator

import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.ImageGenerator

expect class ImageObfuscator : ImageGenerator<CameraImage> {
    override suspend fun generate(from: CameraImage): Result<CameraImage>
}
