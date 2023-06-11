package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageLabels

actual class LabelImageAnalyzer : ImageAnalyzer<ImageLabels> {

    actual override suspend fun analyze(image: CameraImage): Result<ImageLabels> {

        return Result.failure(NotImplementedError())
    }
}
