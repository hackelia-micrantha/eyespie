package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings

expect class MatchCaptureAnalyzer : CaptureAnalyzer<ImageEmbeddings> {
    override suspend fun analyze(image: CameraImage): Result<ImageEmbeddings>
}
