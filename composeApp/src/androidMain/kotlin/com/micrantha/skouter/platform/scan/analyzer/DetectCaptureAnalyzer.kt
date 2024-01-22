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
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.platform.scan.CameraAnalyzerConfig
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.baseOptions
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer

private const val MODEL_ASSET = "models/detection/image.tflite"

typealias DetectionAnalyzerConfig = CameraAnalyzerConfig<DetectProof, ObjectDetectorOptions.Builder, ObjectDetector, ObjectDetectorResult>

actual class DetectCaptureAnalyzer(
    context: Context,
) : DetectAnalyzer(context), CaptureAnalyzer<DetectProof> {

    private val client by lazy {
        super.client {
            setRunningMode(IMAGE)
        }
    }

    actual override suspend fun analyze(image: CameraImage): Result<DetectProof> = try {
        val result = client.detect(
            image.asMPImage(),
            image.processingOptions
        )
        Result.success(super.map(result))
    } catch (err: Throwable) {
        Result.failure(err)
    }
}

class DetectStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<DetectProof>,
) : DetectAnalyzer(context), StreamAnalyzer {

    private val client by lazy {
        super.client {
            setRunningMode(LIVE_STREAM)
            setResultListener(::onResult)
            setErrorListener(callback::onAnalyzerError)
        }
    }

    override fun analyze(image: CameraImage) {
        client.detectAsync(
            image.asMPImage(), image.processingOptions, image.timestamp
        )
    }

    private fun onResult(result: ObjectDetectorResult, input: MPImage) {
        callback.onAnalyzerResult(super.map(result))
    }
}

abstract class DetectAnalyzer(private val context: Context) : DetectionAnalyzerConfig {
    override fun map(result: ObjectDetectorResult): DetectProof {
        return result.detections().map(::detect).toSet()
    }

    private fun detect(obj: Detection) = DetectClue(
        labels = obj.categories().map { LabelClue(it.categoryName(), it.score()) }.toSet(),
        data = obj.boundingBox().toRect().toComposeRect(),
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
