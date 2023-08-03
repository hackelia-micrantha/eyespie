package com.micrantha.skouter.platform.scan

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.skouter.platform.scan.components.CameraScannerDispatch

@Composable
expect fun CameraScanner(
    modifier: Modifier,
    onCameraImage: CameraScannerDispatch
)
