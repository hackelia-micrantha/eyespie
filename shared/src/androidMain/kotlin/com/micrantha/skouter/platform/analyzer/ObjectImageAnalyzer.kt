package com.micrantha.skouter.platform.analyzer

import androidx.compose.ui.graphics.toComposeRect
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageAnalyzer
import com.micrantha.skouter.platform.ImageLabel
import com.micrantha.skouter.platform.ImageObject
import com.micrantha.skouter.platform.ImageObjects
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual class ObjectImageAnalyzer : ImageAnalyzer<ImageObjects> {
    private val client by lazy {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            //.enableMultipleObjects()
            .enableClassification()
            .build()
        ObjectDetection.getClient(options)
    }

    actual override suspend fun analyze(image: CameraImage): Result<ImageObjects> =
        suspendCoroutine { continuation ->
            try {
                val input = InputImage.fromMediaImage(image.image, image.rotation)

                val result = Tasks.await(client.process(input))

                continuation.resume(Result.success(result.map(::map)))
            } catch (err: Throwable) {
                continuation.resumeWithException(err)
            }
        }

    private fun map(obj: DetectedObject) = ImageObject(
        labels = obj.labels.map { ImageLabel(it.text, it.confidence) },
        box = obj.boundingBox.toComposeRect(),
        id = obj.trackingId ?: -1
    )
}
