package com.micrantha.skouter.platform.scan

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.media.Image
import androidx.compose.ui.graphics.asImageBitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.framework.image.MediaImageBuilder
import com.google.mediapipe.tasks.vision.core.ImageProcessingOptions
import com.micrantha.bluebell.platform.toByteArray

actual class CameraImage(
    private val data: Image? = null,
    actual val width: Int,
    actual val height: Int,
    val rotation: Int,
    val timestamp: Long,
    val regionOfInterest: RectF? = null,
) {

    private lateinit var bitmapBuffer: Bitmap
    private lateinit var mediaImage: MPImage

    actual fun toImageBitmap() = toBitmap().asImageBitmap()

    actual fun toByteArray() = toBitmap().toByteArray()

    val processingOptions: ImageProcessingOptions by lazy {
        ImageProcessingOptions.builder().apply {
            setRotationDegrees(rotation)
            regionOfInterest?.let { setRegionOfInterest(it) }
        }.build()
    }

    fun asMPImage(): MPImage {
        if (::mediaImage.isInitialized) return mediaImage

        mediaImage = data?.let {
            MediaImageBuilder(it).build()
        } ?: BitmapImageBuilder(toBitmap()).build()

        return mediaImage
    }

    fun toBitmap(): Bitmap {
        if (::bitmapBuffer.isInitialized) return bitmapBuffer

        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            copyPixelsFromBuffer(data!!.planes[0].buffer)
        }

        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())

        bitmapBuffer = Bitmap.createBitmap(
            result,
            0,
            0,
            width,
            height,
            matrix,
            false
        )

        return bitmapBuffer
    }
}
