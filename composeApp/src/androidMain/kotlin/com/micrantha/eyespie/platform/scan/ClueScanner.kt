package com.micrantha.eyespie.platform.scan

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import com.micrantha.eyespie.platform.scan.components.ClueScannerDispatch

@Composable
actual fun ClueScanner(
    modifier: Modifier,
    regionOfInterest: Rect?,
    onCameraImage: ClueScannerDispatch
) {
    CameraScanner(modifier = modifier, regionOfInterest = regionOfInterest) {
        
    }
}