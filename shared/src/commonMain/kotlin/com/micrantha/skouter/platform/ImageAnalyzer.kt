package com.micrantha.skouter.platform

import androidx.compose.ui.geometry.Rect
import com.micrantha.bluebell.domain.arch.Action

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
    val rect: Rect,
    val labels: ImageLabels
)

typealias ImageObjects = List<ImageObject>

data class ImageColor(
    val name: String,
    val rgb: Int,
)

typealias ImageColors = List<ImageColor>

data class ImageCaptured(val image: CameraImage) : Action

data class ImageSegment(
    val mask: CameraImage
)

typealias ImageSegments = List<ImageSegment>

data class ImageEmbedding(
    val embedding: ByteArray
)

typealias ImageEmbeddings = List<ImageEmbedding>
