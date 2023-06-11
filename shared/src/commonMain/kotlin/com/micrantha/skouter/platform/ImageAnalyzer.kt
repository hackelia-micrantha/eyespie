package com.micrantha.skouter.platform

import androidx.compose.ui.geometry.Rect

interface ImageAnalyzer<T> {
    suspend fun analyze(image: CameraImage): Result<T>
}

data class ImageLabel(
    val data: String,
    val confidence: Float,
)

typealias ImageLabels = List<ImageLabel>

data class ImageObject(
    val id: Int,
    val box: Rect,
    val labels: ImageLabels
)

typealias ImageObjects = List<ImageObject>

data class ImageColor(
    val name: String,
    val rgb: Int,
)

typealias ImageColors = List<ImageColor>
