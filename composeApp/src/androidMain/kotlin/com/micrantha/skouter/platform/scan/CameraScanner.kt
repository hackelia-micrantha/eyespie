package com.micrantha.skouter.platform.scan

import android.util.Size
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.micrantha.skouter.platform.scan.components.CameraScannerDispatch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.ceil


private fun PreviewView.enableZoom(camera: Camera) {

    val scaleGestureDetector =
        ScaleGestureDetector(context, object : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                return camera.cameraInfo.zoomState.value?.let { zoom ->
                    val scale = zoom.zoomRatio * detector.scaleFactor
                    camera.cameraControl.setZoomRatio(scale)
                    true
                } ?: false
            }
        })

    setOnTouchListener { view, event ->
        view.performClick()
        scaleGestureDetector.onTouchEvent(event)
    }
}

private fun CameraSelector.toggle() = when (this) {
    CameraSelector.DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
    else -> CameraSelector.DEFAULT_BACK_CAMERA
}

@Composable
actual fun CameraScanner(
    modifier: Modifier,
    onCameraImage: CameraScannerDispatch
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val config = context.resources.displayMetrics
    val statusBarHeight = ceil((24 * config.density).toDouble()).toInt()

    val screenSize = Size(config.widthPixels, config.heightPixels + statusBarHeight)

    val scope = rememberCoroutineScope()

    val analyzer = remember { CameraAnalyzer(onCameraImage, scope) }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setTargetResolution(screenSize)
            .build()
    }
    val executor = ContextCompat.getMainExecutor(context)
    val cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    val previewView = remember {
        PreviewView(context).apply {
            this.scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    LaunchedEffect(cameraSelector) {
        val cameraProvider = suspendCoroutine<ProcessCameraProvider> { continuation ->
            ProcessCameraProvider.getInstance(context).also { future ->
                future.addListener({
                    continuation.resume(future.get())
                }, executor)
            }
        }

        val useCases = UseCaseGroup.Builder()

        useCases.addUseCase(imageCapture)

        val previewUseCase = Preview.Builder()
            .setTargetResolution(screenSize)
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

        useCases.addUseCase(previewUseCase)

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(screenSize)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(executor, analyzer)
            }
        useCases.addUseCase(imageAnalysis)

        runCatching {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                useCases.build()
            ).apply {
                previewView.enableZoom(this)
            }
        }
    }
    AndroidView(
        modifier = modifier,
        factory = { previewView },
    )
}
