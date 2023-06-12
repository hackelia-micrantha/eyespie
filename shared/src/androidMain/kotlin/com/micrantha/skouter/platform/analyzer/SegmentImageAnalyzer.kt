package com.micrantha.skouter.platform.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageSegment
import com.micrantha.skouter.platform.ImageSegments
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.segmenter.ColoredLabel
import org.tensorflow.lite.task.vision.segmenter.ImageSegmenter
import org.tensorflow.lite.task.vision.segmenter.ImageSegmenter.ImageSegmenterOptions
import org.tensorflow.lite.task.vision.segmenter.OutputType.CATEGORY_MASK
import org.tensorflow.lite.task.vision.segmenter.OutputType.CONFIDENCE_MASK
import org.tensorflow.lite.task.vision.segmenter.Segmentation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.max

actual class SegmentImageAnalyzer(
    private val context: Context,
) : ImageAnalyzer<ImageSegments> {

    private val options by lazy {
        ImageSegmenterOptions.builder()
            //.setBaseOptions(BaseOptions.builder().useGpu().build())
            .setBaseOptions(BaseOptions.builder().build())
            .setOutputType(CATEGORY_MASK)
            .build()
    }
    private val imageSegmenter: ImageSegmenter by lazy {
        ImageSegmenter.createFromFileAndOptions(
            context,
            "lite-model_deeplabv3_1_metadata_2.tflite",
            options
        )
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageSegments> =
        suspendCoroutine { continuation ->
            try {
                val imageProcessor = ImageProcessor.Builder()
                    .add(Rot90Op(-image.rotation / 90)).build()

                val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image.bitmap))

                val segments = imageSegmenter.segment(tensorImage)

                val result = segments.flatMap {
                    when (options.outputType!!) {
                        CATEGORY_MASK ->
                            category(it, image)
                        CONFIDENCE_MASK -> confidence(it, image)
                    }
                }

                continuation.resume(Result.success(result))
            } catch (err: Throwable) {
                continuation.resume(Result.failure(err))
            }
        }

    private fun confidence(data: Segmentation, image: CameraImage): ImageSegments {
        throw NotImplementedError()
    }

    private fun category(data: Segmentation, image: CameraImage): ImageSegments {

        val colorLabels = data.coloredLabels

        return data.masks.mapNotNull { mask ->

            val maskArray = mask.buffer.array()

            val labels = labels(maskArray, colorLabels)

            if (labels.isEmpty()) {
                return@mapNotNull null
            }

            val pixels = pixels(maskArray, colorLabels)

            val scaled = scale(pixels, mask.width, mask.height, image)

            ImageSegment(labels, CameraImage(scaled, image.rotation))
        }
    }

    private fun labels(maskArray: ByteArray, maskLabels: List<ColoredLabel>): Set<String> {
        val result = mutableSetOf<String>()
        for (i in maskArray.indices) {
            val j = maskArray[i].toInt()
            if (j == MASK_BACKGROUND) {
                continue
            }
            result.add(maskLabels[j].getlabel().replaceFirstChar { it.uppercaseChar() })
        }
        return result
    }

    private fun pixels(maskArray: ByteArray, maskLabels: List<ColoredLabel>): IntArray {
        val pixels = IntArray(maskArray.size)

        for (i in maskArray.indices) {
            val j = maskArray[i].toInt()
            if (j == MASK_BACKGROUND) {
                pixels[i] = Color.TRANSPARENT
                continue
            }
            val label = maskLabels[j]
            val color = label.color
            pixels[i] = Color.argb(128f, color.red(), color.green(), color.blue())
        }
        return pixels
    }

    private fun scale(pixels: IntArray, width: Int, height: Int, image: CameraImage): Bitmap {
        val maskImage = Bitmap.createBitmap(
            pixels,
            width,
            height,
            Bitmap.Config.ARGB_8888
        )

        val scaleFactor =
            max(width * 1f / image.width, height * 1f / image.height)
        val scaleWidth = (image.width * scaleFactor).toInt()
        val scaleHeight = (image.height * scaleFactor).toInt()

        return Bitmap.createScaledBitmap(maskImage, scaleWidth, scaleHeight, false)
    }

    companion object {
        const val MASK_BACKGROUND = 0
    }
}
