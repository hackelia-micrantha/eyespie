package com.micrantha.skouter.platform.scan.model

import androidx.compose.ui.geometry.Rect

data class ImageDetection(
    val rect: Rect,
    val labels: ImageLabels
)

typealias ImageObjects = List<ImageDetection>
