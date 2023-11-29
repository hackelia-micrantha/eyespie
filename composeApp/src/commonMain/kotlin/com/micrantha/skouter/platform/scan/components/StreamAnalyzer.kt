package com.micrantha.skouter.platform.scan.components

import com.micrantha.skouter.platform.scan.CameraImage

interface StreamAnalyzer {
    fun analyze(image: CameraImage)
}
