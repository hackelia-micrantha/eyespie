package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageObjects

expect class ObjectImageAnalyzer : ImageAnalyzer<ImageObjects> {
    override suspend fun analyze(image: CameraImage): Result<ImageObjects>
}
