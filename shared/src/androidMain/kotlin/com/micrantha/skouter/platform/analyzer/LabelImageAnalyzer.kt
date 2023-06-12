package com.micrantha.skouter.platform.analyzer

import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageLabel
import com.micrantha.skouter.platform.ImageLabels
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.mlkit.vision.label.ImageLabel as MLImageLabel

actual class LabelImageAnalyzer : ImageAnalyzer<ImageLabels> {

    private val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build()
    )

    actual override suspend fun analyze(image: CameraImage): Result<ImageLabels> =
        suspendCoroutine { continuation ->
            try {
                val input = InputImage.fromBitmap(image.bitmap, image.rotation)

                val result = Tasks.await(labeler.process(input))

                continuation.resume(Result.success(result.map(::map)))
            } catch (err: Throwable) {
                continuation.resume(Result.failure(err))
            }
        }

    private fun map(label: MLImageLabel) = ImageLabel(
        confidence = label.confidence,
        data = label.text
    )
}
