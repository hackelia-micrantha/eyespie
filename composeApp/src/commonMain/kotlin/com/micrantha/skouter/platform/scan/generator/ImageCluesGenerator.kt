package com.micrantha.skouter.platform.scan.generator

import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.ImageGenerator

expect class ImageCluesGenerator : ImageGenerator<Clues> {
    override fun generate(from: Clues): Result<CameraImage>
}