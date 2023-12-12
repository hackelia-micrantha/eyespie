package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanSegments
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.segmenter.ImageSegmenter
import org.tensorflow.lite.task.vision.segmenter.ImageSegmenter.ImageSegmenterOptions
import org.tensorflow.lite.task.vision.segmenter.OutputType
import java.nio.ByteBuffer

private const val MODEL_ASSET = "models/segmentation/image.tflite"

actual class SegmentCaptureAnalyzer(
    context: Context,
) : AndroidCaptureAnalyzer<ScanSegments>(), CaptureAnalyzer<ScanSegments>,
    StreamAnalyzer<ScanSegments> {

    private val client by lazy {
        val options = ImageSegmenterOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .useGpu()
                    .build()
            )
            .setOutputType(OutputType.CATEGORY_MASK)
            .build()
        ImageSegmenter.createFromFileAndOptions(context, MODEL_ASSET, options)
    }

    actual override suspend fun analyzeCapture(image: CameraImage): Result<ScanSegments> = try {
        val result =
            client.segment(TensorImage.fromBitmap(image.bitmap), image.processingOptions())
        val masks = result.flatMap { it.masks }
            .map {
                colorizePixels(it, image.rotation, image.timestamp)
            }
        Result.success(masks)
    } catch (err: Throwable) {
        Result.failure(err)
    }

    private fun colorizePixels(image: TensorImage, rotation: Int, timestamp: Long): CameraImage {
        val pixels = colorizePixels(image.buffer)

        val coloredMaskImage = Bitmap.createBitmap(
            pixels,
            image.width,
            image.height,
            Bitmap.Config.ARGB_8888
        )
        return CameraImage(coloredMaskImage, rotation, timestamp)
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
