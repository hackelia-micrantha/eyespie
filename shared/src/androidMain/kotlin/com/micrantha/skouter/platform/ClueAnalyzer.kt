package com.micrantha.skouter.platform

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.skouter.domain.repository.ClueRepository
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageCaptured
import com.micrantha.skouter.ui.scan.preview.ScanAction.LabelScanned
import com.micrantha.skouter.ui.scan.preview.ScanAction.ObjectScanned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext

class ClueAnalyzer(
    private val dispatcher: Dispatcher,
    private val repository: ClueRepository,
    context: CoroutineContext = Dispatchers.Default,
    private val scope: CoroutineScope = CoroutineScope(context) + Job()
) : ImageAnalysis.Analyzer, Dispatcher by dispatcher {

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val rotation = image.imageInfo.rotationDegrees

        val cameraImage = with(image.image!!) {
            CameraImage(this, rotation)
        }

        dispatch(ImageCaptured(cameraImage))

        scope.launch {

            identifyClues(cameraImage)

            image.close()
        }
    }

    private suspend fun identifyClues(image: CameraImage) {
        repository.label(image).onSuccess { labels ->
            dispatch(LabelScanned(labels))
        }
        repository.recognize(image).onSuccess { obj ->
            dispatch(ObjectScanned(obj))
        }
    }
}
