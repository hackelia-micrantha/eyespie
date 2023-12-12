package com.micrantha.skouter.platform.scan.components

import com.micrantha.skouter.platform.scan.CameraImage

interface CaptureAnalyzer<out T> {
    suspend fun analyzeCapture(image: CameraImage): Result<T>
}
