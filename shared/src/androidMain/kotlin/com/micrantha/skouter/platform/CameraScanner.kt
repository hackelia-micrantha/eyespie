package com.micrantha.skouter.platform

import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import org.kodein.di.compose.rememberFactory

@Composable
actual fun CameraScanner(
    modifier: Modifier,
    enabled: Boolean,
    onCameraImage: ImageAnalyzerCallback
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val analyzer by rememberFactory<CameraAnalyzerOptions, CameraAnalyzer>()

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = ContextCompat.getMainExecutor(ctx)

            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
            val useCases = UseCaseGroup.Builder()

            useCases.addUseCase(preview)

            val cameraAnalyzerOptions = CameraAnalyzerOptions(
                callback = onCameraImage,
                image = { previewView.bitmap }
            )

            if (enabled) {
                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply {
                        setAnalyzer(executor, analyzer(cameraAnalyzerOptions))
                    }
                useCases.addUseCase(imageAnalysis)
            }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    useCases.build()
                )

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

                previewView.setOnTouchListener { view, event ->
                    view.performClick()
                    scaleGestureDetector.onTouchEvent(event)
                }
            }, executor)
            previewView
        },
    )
}
