package com.micrantha.skouter.ui.scan.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import com.micrantha.skouter.ui.scan.view.ScanBox
import com.micrantha.skouter.ui.scan.view.ScanMask
import com.micrantha.skouter.ui.scan.view.ScanOverlay

@OptIn(ExperimentalTextApi::class)
@Composable
fun BoxWithConstraintsScope.ScannedOverlays(
    modifier: Modifier = Modifier,
    data: List<ScanOverlay>
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
