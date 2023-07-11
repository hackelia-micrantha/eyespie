package com.micrantha.skouter.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun CameraScanner(
    modifier: Modifier,
    onCameraImage: ImageAnalyzerCallback
)
