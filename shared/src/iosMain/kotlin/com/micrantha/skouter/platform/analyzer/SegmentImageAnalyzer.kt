package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageSegments

actual class SegmentImageAnalyzer : ImageAnalyzer<ImageSegments> {
    actual override suspend fun analyze(image: CameraImage): Result<ImageSegments> {
        return Result.failure(NotImplementedError())
    }
}
