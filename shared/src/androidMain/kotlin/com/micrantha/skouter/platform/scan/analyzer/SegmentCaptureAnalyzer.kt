package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.ByteBufferExtractor
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenter
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenter.ImageSegmenterOptions
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenterResult
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageSegment
import com.micrantha.skouter.platform.scan.model.ImageSegments

private const val MODEL_ASSET = "models/segmentation/image.tflite"

actual class SegmentCaptureAnalyzer(
    context: Context,
) : CaptureAnalyzer<ImageSegments> {

    private val client by lazy {
        baseOptions().setRunningMode(IMAGE).client(context)
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageSegments> {
        val input = BitmapImageBuilder(image.bitmap).build()

        return client.segment(input).analyze()
    }
}

actual class SegmentStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<ImageSegments>
) : StreamAnalyzer {

    private val client by lazy {
        baseOptions().setRunningMode(LIVE_STREAM)
            .setResultListener(::onResult)
            .setErrorListener(callback::onAnalyzerError)
            .client(context)
    }

    actual override fun analyze(image: CameraImage) {
        val input = BitmapImageBuilder(image.bitmap).build()
        client.segmentAsync(input, image.timestamp)
    }

    private fun onResult(result: ImageSegmenterResult, input: MPImage) {
        result.analyze()
            .onSuccess(callback::onAnalyzerResult)
            .onFailure(callback::onAnalyzerError)
    }
}

private fun ImageSegmenterResult.analyze() = try {

    val maskImage = categoryMask().get()

    val byteBuffer = ByteBufferExtractor.extract(maskImage)
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
    val coloredMaskImage = Bitmap.createBitmap(
        pixels,
        maskImage.width,
        maskImage.height,
        Bitmap.Config.ARGB_8888
    )

    Result.success(listOf(ImageSegment(CameraImage(coloredMaskImage, timestampMs()))))
} catch (err: Throwable) {
    Result.failure(err)
}

private const val ALPHA_COLOR = 80

private val labelColors: List<Int>
    get() = listOf(
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

private fun baseOptions() = ImageSegmenterOptions.builder()
    .setBaseOptions(
        BaseOptions.builder()
            .setModelAssetPath(MODEL_ASSET)
            .build()
    )
    .setOutputCategoryMask(true)

private fun ImageSegmenterOptions.Builder.client(context: Context) = let {
    ImageSegmenter.createFromOptions(context, it.build())
}
