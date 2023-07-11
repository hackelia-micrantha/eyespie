package com.micrantha.skouter.platform

import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.flow.Flow
import okio.ByteString

interface ImageAnalyzer<T> {
    suspend fun analyze(image: CameraImage): Result<T>
}

interface ImageFlowAnalyzer<T> {
    val results: Flow<ImageAnalyzerResult<T>>
}

typealias ImageAnalyzerCallback = suspend (CameraImage) -> Unit

data class ImageAnalyzerResult<T>(
    val image: CameraImage,
    val data: T
)

data class ImageLabel(
    val data: String,
    val confidence: Float,
)

typealias ImageLabels = List<ImageLabel>

data class ImageObject(
    val rect: Rect,
    val labels: ImageLabels
)

typealias ImageObjects = List<ImageObject>

data class ImageColor(
    val name: String,
    val rgb: Int,
)

typealias ImageColors = List<ImageColor>

data class ImageSegment(
    val mask: CameraImage
)

typealias ImageSegments = List<ImageSegment>

typealias ImageEmbedding = ByteString

typealias ImageEmbeddings = List<ImageEmbedding>
