package com.micrantha.skouter.ui.scan.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import com.micrantha.skouter.ui.scan.preview.ScanBox
import com.micrantha.skouter.ui.scan.preview.ScanMask
import com.micrantha.skouter.ui.scan.preview.ScanOverlay

@OptIn(ExperimentalTextApi::class)
@Composable
fun BoxScope.ScannedOverlays(
    modifier: Modifier = Modifier,
    data: List<ScanOverlay>
) {
    val measurer = rememberTextMeasurer()

    val textStyle = MaterialTheme.typography.labelMedium.copy(color = Color.Red)

    val matrix = remember { Matrix() }

    Canvas(
        modifier = modifier.align(Alignment.TopCenter).fillMaxSize()
    ) {
        data.forEach {
            when (it) {
                is ScanBox -> drawScanBox(it, matrix, textStyle, measurer)
                is ScanMask -> drawScanMask(it)
            }
        }
    }
}
