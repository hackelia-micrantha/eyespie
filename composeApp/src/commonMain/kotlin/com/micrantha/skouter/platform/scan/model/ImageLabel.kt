package com.micrantha.skouter.platform.scan.model

data class ImageLabel(
    val data: String,
    val confidence: Float,
)

typealias ImageLabels = List<ImageLabel>
