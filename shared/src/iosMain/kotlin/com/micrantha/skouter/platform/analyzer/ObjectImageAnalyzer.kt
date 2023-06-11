package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageObjects

actual class ObjectImageAnalyzer : ImageAnalyzer<ImageObjects> {
    actual override suspend fun analyze(image: CameraImage): Result<ImageObjects> {
        return Result.failure(NotImplementedError())
    }
}
