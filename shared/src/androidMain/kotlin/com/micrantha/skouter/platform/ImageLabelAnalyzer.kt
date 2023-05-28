package com.micrantha.skouter.platform

import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.micrantha.skouter.data.clue.model.LabelResponse
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class ImageLabelAnalyzer {

    private val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build()
    )

    actual suspend fun analyze(image: CameraImage): Result<List<LabelResponse>> =
        suspendCoroutine { continuation ->
            try {
                val input = InputImage.fromMediaImage(image.image, image.rotation)

                val result = Tasks.await(labeler.process(input))

                continuation.resume(Result.success(result.map {
                    LabelResponse(
                        it.confidence,
                        it.text
                    )
                }))
            } catch (err: Throwable) {
                continuation.resume(Result.failure(err))
            }
        }
}
