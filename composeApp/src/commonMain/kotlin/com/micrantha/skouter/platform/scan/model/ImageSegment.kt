package com.micrantha.skouter.platform.scan.model

import com.micrantha.skouter.platform.scan.CameraImage

data class ImageSegment(
    val mask: CameraImage
)

typealias ImageSegments = List<ImageSegment>
