package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ScanObjects
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector

private const val MODEL_ASSET = "models/detection/image.tflite"

actual class ObjectCaptureAnalyzer(
    context: Context,
) : AndroidCaptureAnalyzer<ScanObjects>(), CaptureAnalyzer<ScanObjects>,
    StreamAnalyzer<ScanObjects> {

    private val client by lazy {
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .useGpu()
                    .build()
            )
            .setScoreThreshold(0.7f)
            .build()
        ObjectDetector.createFromFileAndOptions(context, MODEL_ASSET, options)
    }

    actual override suspend fun analyzeCapture(image: CameraImage): Result<ScanObjects> = try {
        val result = client.detect(
            TensorImage.fromBitmap(image.bitmap), image.processingOptions()
        )
        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
