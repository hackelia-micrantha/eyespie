package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageColors

expect class ColorImageAnalyzer : ImageAnalyzer<ImageColors> {
    override suspend fun analyze(image: CameraImage): Result<ImageColors>
}
