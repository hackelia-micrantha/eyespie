package com.micrantha.skouter.platform.scan.components

import com.micrantha.skouter.platform.scan.CameraImage

interface StreamAnalyzer<T> {
    fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<T>)
}
