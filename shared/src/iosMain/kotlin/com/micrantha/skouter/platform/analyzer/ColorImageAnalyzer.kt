package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageColors

actual class ColorImageAnalyzer : ImageAnalyzer<ImageColors> {
    actual override suspend fun analyze(image: CameraImage): Result<ImageColors> {
        return Result.failure(NotImplementedError())
    }
}
