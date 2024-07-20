package com.micrantha.eyespie.platform.scan.components

import com.micrantha.eyespie.platform.scan.CameraImage

interface CaptureAnalyzer<out T> {
    suspend fun analyze(image: CameraImage): Result<T>
}
