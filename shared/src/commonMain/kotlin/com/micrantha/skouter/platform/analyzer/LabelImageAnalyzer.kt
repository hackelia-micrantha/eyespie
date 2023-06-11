package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageLabels

expect class LabelImageAnalyzer : ImageAnalyzer<ImageLabels> {
    override suspend fun analyze(image: CameraImage): Result<ImageLabels>
}
