package com.micrantha.eyespie.platform.scan.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import com.google.mediapipe.framework.image.ByteBufferExtractor
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenter
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenter.ImageSegmenterOptions
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenterResult
import com.micrantha.bluebell.app.Log
import com.micrantha.eyespie.domain.entities.SegmentClue
import com.micrantha.eyespie.domain.entities.SegmentProof
import com.micrantha.eyespie.platform.scan.CameraAnalyzerConfig
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.baseOptions
import com.micrantha.eyespie.platform.scan.components.AnalyzerCallback
import com.micrantha.eyespie.platform.scan.components.CaptureAnalyzer
import com.micrantha.eyespie.platform.scan.components.StreamAnalyzer
import java.nio.ByteBuffer

private const val MODEL_ASSET = "models/segmentation/image.tflite"
typealias SegmentAnalyzerConfig = CameraAnalyzerConfig<SegmentProof, ImageSegmenterOptions.Builder, ImageSegmenter, ImageSegmenterResult>

actual class SegmentCaptureAnalyzer(
    context: Context,
) : SegmentAnalyzer(context), CaptureAnalyzer<SegmentProof> {

    private val client by lazy {
        super.client { setRunningMode(IMAGE) }
    }

    actual override suspend fun analyze(image: CameraImage): Result<SegmentProof> = try {
        val result = client.segment(
            image.asMPImage(), image.processingOptions
        )
        Result.success(super.map(result))
    } catch (err: Throwable) {
        Log.e("analyzer", err) { "unable to segment image" }
        Result.failure(err)
    }
}

class SegmentStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<SegmentProof>,
) : SegmentAnalyzer(context), StreamAnalyzer {

    private val client by lazy {
        super.client {
            setRunningMode(LIVE_STREAM)
            setResultListener(::onResult)
            setErrorListener(callback::onAnalyzerError)
        }
    }

    override fun analyze(image: CameraImage) {
        client.segmentAsync(
            image.asMPImage(),
            image.processingOptions,
            image.timestamp
        )
    }

    private fun onResult(result: ImageSegmenterResult, input: MPImage) {
        callback.onAnalyzerResult(super.map(result))
    }
}

abstract class SegmentAnalyzer(private val context: Context) : SegmentAnalyzerConfig {
    override fun map(result: ImageSegmenterResult): SegmentProof {
        val categoryMask = result.categoryMask()
        return if (categoryMask.isPresent) emptyList() else listOf(mask(categoryMask.get()))
    }

    override fun client(block: ImageSegmenterOptions.Builder.() -> Unit): ImageSegmenter {
        val options = ImageSegmenterOptions.builder()
            .setBaseOptions(baseOptions(MODEL_ASSET))
            .setOutputCategoryMask(true)
            .apply(block)
            .build()
        return ImageSegmenter.createFromOptions(context, options)
    }

    private fun mask(maskImage: MPImage): SegmentClue {
        val byteBuffer = ByteBufferExtractor.extract(maskImage)

        val pixels = colorizePixels(byteBuffer)

        val coloredMaskImage = Bitmap.createBitmap(
            pixels,
            maskImage.width,
            maskImage.height,
            Bitmap.Config.ARGB_8888
        )

        return SegmentClue(coloredMaskImage.asImageBitmap())
    }

}

private const val ALPHA_COLOR = 80

private fun colorizePixels(byteBuffer: ByteBuffer): IntArray {
    // Create the mask bitmap with colors and the set of detected labels.
    val pixels = IntArray(byteBuffer.capacity())
    for (i in pixels.indices) {
        val index = byteBuffer.get(i).toUInt() % 20U
        val color = with(labelColors[index.toInt()]) {
            Color.argb(
                ALPHA_COLOR,
                Color.red(this),
                Color.green(this),
                Color.blue(this)
            )
        }
        pixels[i] = color
    }
    return pixels
}

private val labelColors: Array<Int> by lazy {
    arrayOf(
        -16777216,
        -8388608,
        -16744448,
        -8355840,
        -16777088,
        -8388480,
        -16744320,
        -8355712,
        -12582912,
        -4194304,
        -12550144,
        -4161536,
        -12582784,
        -4194176,
        -12550016,
        -4161408,
        -16760832,
        -8372224,
        -16728064,
        -8339456,
        -16760704
    )
}
