package com.micrantha.eyespie.platform.scan.components

import com.micrantha.eyespie.platform.scan.CameraImage

interface StreamAnalyzer {
    fun analyze(image: CameraImage)
}
