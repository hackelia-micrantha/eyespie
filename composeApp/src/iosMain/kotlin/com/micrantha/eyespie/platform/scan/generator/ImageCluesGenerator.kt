package com.micrantha.eyespie.platform.scan.generator

import com.micrantha.eyespie.domain.entities.Clues
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.ImageGenerator

actual class ImageCluesGenerator : ImageGenerator<Clues> {
    actual override fun generate(from: Clues): Result<CameraImage> {
        TODO("not implemented")
    }
}