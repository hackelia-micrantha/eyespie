package com.micrantha.eyespie.platform.scan.generator

import com.micrantha.eyespie.domain.entities.Clues
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.ImageGenerator

actual class ImageCluesGenerator : ImageGenerator<Clues> {
    actual override fun generate(from: Clues): Result<CameraImage> {
        // incorporate a DAL-E tensor model
        // and use clues to generate an image
        TODO("not implemented")
    }
}