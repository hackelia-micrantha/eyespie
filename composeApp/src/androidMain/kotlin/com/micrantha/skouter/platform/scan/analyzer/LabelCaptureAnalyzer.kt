package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanLabels
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

private const val MODEL_ASSET = "models/classification/image.tflite"

actual class LabelCaptureAnalyzer(
    context: Context
) : AndroidCaptureAnalyzer<ScanLabels>(), CaptureAnalyzer<ScanLabels>, StreamAnalyzer<ScanLabels> {

    private val classifier by lazy {
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .useGpu()
                    .build()
            )
            .setScoreThreshold(0.6f)
            .build()
        ImageClassifier.createFromFileAndOptions(context, MODEL_ASSET, options)
    }

    actual override suspend fun analyzeCapture(image: CameraImage): Result<ScanLabels> = try {
        val result =
            classifier.classify(TensorImage.fromBitmap(image.bitmap), image.processingOptions())
        val categories = result.flatMap { it.categories }
        Result.success(categories)
    } catch (ex: Throwable) {
        Result.failure(ex)
    }
}
