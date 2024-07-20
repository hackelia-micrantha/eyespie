package com.micrantha.eyespie.platform.scan.generator

import com.micrantha.eyespie.domain.model.Clues
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.components.ImageGenerator

expect class ImageCluesGenerator : ImageGenerator<Clues> {
    override fun generate(from: Clues): Result<CameraImage>
}