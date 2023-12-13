package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import androidx.compose.ui.graphics.toComposeRect
import androidx.core.graphics.toRect
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.components.containers.Detection
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector.ObjectDetectorOptions
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult
import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.baseOptions
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageDetection
import com.micrantha.skouter.platform.scan.model.ImageLabel
import com.micrantha.skouter.platform.scan.model.ImageObjects

private const val MODEL_ASSET = "models/detection/image.tflite"

typealias DetectionAnalyzerConfig = CameraAnalyzerConfig<ImageObjects, ObjectDetectorOptions.Builder, ObjectDetector, ObjectDetectorResult>

actual class DetectCaptureAnalyzer(
    context: Context,
    private val config: DetectionAnalyzerConfig = config(context)
) : CaptureAnalyzer<ImageObjects> {

    private val client by lazy {
        config.client {
            setRunningMode(IMAGE)
        }
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageObjects> = try {
        val result = client.detect(image.asMPImage())
        Result.success(config.map(result))
    } catch (err: Throwable) {
        Result.failure(err)
    }
}

class DetectStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<ImageObjects>,
    private val config: DetectionAnalyzerConfig = config(context)
) : StreamAnalyzer {

    private val client by lazy {
        config.client {
            setRunningMode(LIVE_STREAM)
            setResultListener(::onResult)
            setErrorListener(callback::onAnalyzerError)
        }
    }

    override fun analyze(image: CameraImage) {
        client.detectAsync(image.asMPImage(), image.timestamp)
    }

    private fun onResult(result: ObjectDetectorResult, input: MPImage) {
        callback.onAnalyzerResult(config.map(result))
    }
}

private fun config(context: Context): DetectionAnalyzerConfig = object : DetectionAnalyzerConfig {
    override fun map(result: ObjectDetectorResult): ImageObjects {
        return result.detections().map(::detect)
    }

    private fun detect(obj: Detection) = ImageDetection(
        labels = obj.categories().map { ImageLabel(it.categoryName(), it.score()) },
        rect = obj.boundingBox().toRect().toComposeRect(),
    )

    override fun client(block: ObjectDetectorOptions.Builder.() -> Unit): ObjectDetector {
        val options = ObjectDetectorOptions.builder()
            .setBaseOptions(baseOptions(MODEL_ASSET))
            .setScoreThreshold(0.7f)
            .apply(block)
            .build()
        return ObjectDetector.createFromOptions(context, options)
    }
}
