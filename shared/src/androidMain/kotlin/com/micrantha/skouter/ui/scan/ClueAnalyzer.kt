package com.micrantha.skouter.ui.scan

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.skouter.domain.model.CameraImage
import com.micrantha.skouter.domain.repository.ClueRepository
import com.micrantha.skouter.ui.scan.ScanAction.ImageCaptured
import com.micrantha.skouter.ui.scan.ScanAction.LabelScanned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext

class ClueAnalyzer(
    private val dispatch: Dispatch,
    private val repository: ClueRepository,
    context: CoroutineContext = Dispatchers.Default,
    private val scope: CoroutineScope = CoroutineScope(context) + Job()
) : ImageAnalysis.Analyzer {

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val rotation = image.imageInfo.rotationDegrees

        val cameraImage = CameraImage(image.image!!, rotation)

        dispatch(ImageCaptured(cameraImage))

        identifyClues(cameraImage)
    
        //image.close()
    }

    private fun identifyClues(image: CameraImage) = scope.launch {
        repository.label(image).onSuccess { labels ->
            dispatch(LabelScanned(labels))
        }
    }
}
