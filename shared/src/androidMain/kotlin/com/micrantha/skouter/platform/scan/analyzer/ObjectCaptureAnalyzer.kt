package com.micrantha.skouter.platform.scan.analyzer

import android.content.Context
import androidx.compose.ui.graphics.toComposeRect
import androidx.core.graphics.toRect
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.components.containers.Detection
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE
import com.google.mediapipe.tasks.vision.core.RunningMode.LIVE_STREAM
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector.ObjectDetectorOptions
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.StreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageLabel
import com.micrantha.skouter.platform.scan.model.ImageObject
import com.micrantha.skouter.platform.scan.model.ImageObjects

private const val MODEL_ASSET = "models/detection/image.tflite"

actual class ObjectCaptureAnalyzer(
    context: Context,
) : CaptureAnalyzer<ImageObjects> {

    private val client by lazy {
        baseOptions().setRunningMode(IMAGE).client(context)
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageObjects> = try {
        val input = BitmapImageBuilder(image.bitmap).build()
        client.detect(input).analyze()
    } catch (err: Throwable) {
        Result.failure(err)
    }
}

actual class ObjectStreamAnalyzer(
    context: Context,
    private val callback: AnalyzerCallback<ImageObjects>
) : StreamAnalyzer {

    private val client by lazy {
        baseOptions()
            .setRunningMode(LIVE_STREAM)
            .setResultListener(::onResult)
            .setErrorListener(callback::onAnalyzerError)
            .client(context)
    }

    actual override fun analyze(image: CameraImage) {
        val input = BitmapImageBuilder(image.bitmap).build()
        client.detectAsync(input, image.timestamp)
    }

    private fun onResult(result: ObjectDetectorResult, input: MPImage) {
        result.analyze()
            .onSuccess(callback::onAnalyzerResult)
            .onFailure(callback::onAnalyzerError)
    }
}

private fun ObjectDetectorResult.analyze() =
    Result.success(detections().map(::map))


private fun map(obj: Detection) = ImageObject(
    labels = obj.categories().map { ImageLabel(it.categoryName(), it.score()) },
    rect = obj.boundingBox().toRect().toComposeRect(),
)

private fun baseOptions() = ObjectDetectorOptions.builder()
    .setBaseOptions(
        BaseOptions.builder()
            .setModelAssetPath(MODEL_ASSET)
            .build()
    )
    .setScoreThreshold(0.7f)

private fun ObjectDetectorOptions.Builder.client(context: Context) = let {
    ObjectDetector.createFromOptions(context, it.build())
}
