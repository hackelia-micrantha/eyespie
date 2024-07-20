package com.micrantha.eyespie.platform.scan

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.asImageBitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.framework.image.MediaImageBuilder
import com.google.mediapipe.tasks.vision.core.ImageProcessingOptions
import com.micrantha.bluebell.platform.toByteArray

actual class CameraImage(
    private var data: Image? = null,
    private var _width: Int,
    private var _height: Int,
    private var rotation: Int,
    private var _timestamp: Long,
    private var regionOfInterest: RectF? = null,
) {

    private var bitmapBuffer: Bitmap? = null
    private var mediaImage: MPImage? = null

    actual val width get() = _width
    actual val height get() = _height

    val timestamp get() = _timestamp

    @OptIn(ExperimentalGetImage::class)
    fun copy(image: ImageProxy, region: RectF? = null) {
        _width = image.width
        _height = image.height
        rotation = image.imageInfo.rotationDegrees
        _timestamp = image.imageInfo.timestamp
        regionOfInterest = region ?: regionOfInterest
        data = image.image
        bitmapBuffer = null
        mediaImage = null
    }

    actual fun toImageBitmap() = toBitmap().asImageBitmap()

    actual fun toByteArray() = toBitmap().toByteArray()

    val processingOptions: ImageProcessingOptions by lazy {
        ImageProcessingOptions.builder().apply {
            setRotationDegrees(rotation)
            regionOfInterest?.let { setRegionOfInterest(it) }
        }.build()
    }

    fun asMPImage(): MPImage {
        if (mediaImage != null) return mediaImage!!

        mediaImage = data?.let {
            MediaImageBuilder(it).build()
        } ?: BitmapImageBuilder(toBitmap()).build()

        return mediaImage!!
    }

    fun toBitmap(): Bitmap {
        if (bitmapBuffer != null) return bitmapBuffer!!

        val result = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888).apply {
            copyPixelsFromBuffer(data!!.planes[0].buffer)
        }

        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())

        bitmapBuffer = Bitmap.createBitmap(
            result,
            0,
            0,
            _width,
            _height,
            matrix,
            false
        )

        return bitmapBuffer!!
    }
}
