package com.micrantha.skouter.platform.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.ByteBufferExtractor
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenter
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenter.ImageSegmenterOptions
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageSegment
import com.micrantha.skouter.platform.ImageSegments
import kotlin.math.min

private const val MODEL_ASSET = "models/segmentation/image.tflite"

actual class SegmentImageAnalyzer(
    context: Context,
) : ImageAnalyzer<ImageSegments> {

    private val client by lazy {
        val options = ImageSegmenterOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .setModelAssetPath(MODEL_ASSET)
                    .build()
            )
            .setRunningMode(IMAGE)
            .setOutputCategoryMask(true)
            .build()
        ImageSegmenter.createFromOptions(context, options)
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageSegments> = try {

        val input = BitmapImageBuilder(image.bitmap).build()

        val segments = client.segment(input)

        val maskImage = segments.categoryMask().get()

        segments.confidenceMasks()

        val byteBuffer = ByteBufferExtractor.extract(maskImage)
        // Create the mask bitmap with colors and the set of detected labels.
        val pixels = IntArray(byteBuffer.capacity())
        for (i in pixels.indices) {
            // Using unsigned int here because selfie segmentation returns 0 or 255U (-1 signed)
            // with 0 being the found person, 255U for no label.
            // Deeplab uses 0 for background and other labels are 1-19,
            // so only providing 20 colors from ImageSegmenterHelper -> labelColors
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

        val scaleFactor =
            min(image.width * 1f / maskImage.width, image.height * 1f / maskImage.height)


        val scaleWidth = (maskImage.width * scaleFactor).toInt()
        val scaleHeight = (maskImage.height * scaleFactor).toInt()

        val scaleBitmap = Bitmap.createScaledBitmap(
            coloredMaskImage, scaleWidth, scaleHeight, false
        )

        Result.success(
            listOf(
                ImageSegment(
                    CameraImage(scaleBitmap)
                )
            )
        )
    } catch (err: Throwable) {
        Result.failure(err)
    }


    companion object {
        const val ALPHA_COLOR = 128

        val labelColors = listOf(
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
}
