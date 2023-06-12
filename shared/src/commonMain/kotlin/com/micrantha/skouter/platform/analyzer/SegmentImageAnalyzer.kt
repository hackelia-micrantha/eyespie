package com.micrantha.skouter.platform.analyzer

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageSegments

expect class SegmentImageAnalyzer : ImageAnalyzer<ImageSegments> {
    override suspend fun analyze(image: CameraImage): Result<ImageSegments>
}
