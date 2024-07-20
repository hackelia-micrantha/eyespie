package com.micrantha.eyespie.platform.scan

import android.util.Size
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionSelector.PREFER_HIGHER_RESOLUTION_OVER_CAPTURE_RATE
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.micrantha.eyespie.platform.scan.components.CameraScannerDispatch
import java.util.concurrent.Executors


@Composable
actual fun CameraScanner(
    modifier: Modifier,
    regionOfInterest: Rect?,
    onCameraImage: CameraScannerDispatch
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val scope = rememberCoroutineScope()

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    val analyzer = remember(regionOfInterest, onCameraImage) {
        CameraAnalyzer(regionOfInterest?.toAndroidRectF(), onCameraImage, scope)
    }

    val cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    DisposableEffect(cameraSelector) {
        onDispose {
            cameraProvider?.unbindAll()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PreviewView(context).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        update = { previewView ->
            val context = previewView.context
            val executor = ContextCompat.getMainExecutor(context)

            ProcessCameraProvider.getInstance(context).apply {
                addListener({
                    cameraProvider = get().apply {
                        unbindAll()
                        bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            createCameraUseCases(previewView.surfaceProvider, analyzer)
                        ).apply {
                            previewView.enableZoom(this)
                        }
                    }
                }, executor)
            }

        }
    )
}

private fun createCameraUseCases(
    surfaceProvider: SurfaceProvider,
    analyzer: CameraAnalyzer
): UseCaseGroup {

    val targetScreenSize = Size(1600, 1200)

    val executor = Executors.newSingleThreadExecutor()

    val resolutionSelector = ResolutionSelector.Builder()
        .setAllowedResolutionMode(PREFER_HIGHER_RESOLUTION_OVER_CAPTURE_RATE)
        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
        .setResolutionStrategy(
            ResolutionStrategy(
                targetScreenSize,
                ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
            )
        ).build()

    val imageCapture = ImageCapture.Builder()
        .setResolutionSelector(resolutionSelector)
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
        .build()

    val useCases = UseCaseGroup.Builder()
        .addUseCase(imageCapture)

    val previewUseCase = Preview.Builder()
        .setResolutionSelector(resolutionSelector)
        .build()
        .apply { setSurfaceProvider(surfaceProvider) }

    useCases.addUseCase(previewUseCase)

    val imageAnalysis = ImageAnalysis.Builder()
        .setResolutionSelector(resolutionSelector)
        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .apply { setAnalyzer(executor, analyzer) }
    useCases.addUseCase(imageAnalysis)

    return useCases.build()
}

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

