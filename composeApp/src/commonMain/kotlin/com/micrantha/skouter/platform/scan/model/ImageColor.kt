package com.micrantha.skouter.platform.scan.model

data class ImageColor(
    val name: String,
    val rgb: Int,
)

typealias ImageColors = List<ImageColor>
