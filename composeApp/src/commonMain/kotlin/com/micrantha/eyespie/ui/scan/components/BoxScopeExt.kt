package com.micrantha.eyespie.ui.scan.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import com.micrantha.eyespie.ui.scan.capture.ScanBox
import com.micrantha.eyespie.ui.scan.capture.ScanMask
import com.micrantha.eyespie.ui.scan.capture.ScanOverlay

@OptIn(ExperimentalTextApi::class)
@Composable
fun BoxWithConstraintsScope.ScannedOverlays(
    modifier: Modifier = Modifier,
    data: Collection<ScanOverlay>
) {
    val measurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier.align(Alignment.TopCenter).fillMaxSize()
    ) {
        data.forEach {
            when (it) {
                is ScanBox -> drawScanBox(
                    it,
                    measurer
                )

                is ScanMask -> drawScanMask(it)
            }
        }
    }
}
