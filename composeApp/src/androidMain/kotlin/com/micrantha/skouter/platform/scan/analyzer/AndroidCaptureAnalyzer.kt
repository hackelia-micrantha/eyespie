package com.micrantha.skouter.platform.scan.analyzer

import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

abstract class AndroidCaptureAnalyzer<T>(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default) + Job()
) : CaptureAnalyzer<T>, StreamAnalyzer<T> {

    override fun analyzeStream(image: CameraImage, callback: AnalyzerCallback<T>) {
        scope.launch {
            analyzeCapture(image).onFailure(callback::onAnalyzerError)
                .onSuccess(callback::onAnalyzerResult)
        }
    }

    protected fun loadModelFile(modelFile: String): ByteBuffer {
        val fileChannel = FileInputStream(modelFile).channel
        val modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size())

        // Allocate a direct ByteBuffer for the model
        val directBuffer = ByteBuffer.allocateDirect(modelBuffer.remaining())
        directBuffer.order(ByteOrder.nativeOrder())

        // Copy the contents of the file buffer into the direct buffer
        directBuffer.put(modelBuffer)
        directBuffer.rewind()

        return directBuffer
    }

    fun CameraImage.processingOptions(): ImageProcessingOptions =
        ImageProcessingOptions.builder()
            .setOrientation(
                when (rotation) {
                    90 -> ImageProcessingOptions.Orientation.LEFT_BOTTOM
                    120 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
                    270 -> ImageProcessingOptions.Orientation.RIGHT_TOP
                    else -> ImageProcessingOptions.Orientation.TOP_LEFT
                }
            ).build()
}