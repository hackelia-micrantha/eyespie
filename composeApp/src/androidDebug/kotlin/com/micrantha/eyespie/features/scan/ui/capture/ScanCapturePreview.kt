package com.micrantha.eyespie.features.scan.ui.capture

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import com.micrantha.eyespie.ui.PreviewContext

@Preview(showBackground = true, backgroundColor = 0xFF, widthDp = 200, heightDp = 400)
@Composable
fun ScanCapturePreview() = PreviewContext(
    ScanUiState(
        clues = listOf(
            "clue 1",
            "clue 2"
        ),
        overlays = listOf(
            ScanBox(
                rect = Rect(left = 50F, right = 150F, top = 100F, bottom = 200F),
                label = "test",
                imageWidth = 200,
                imageHeight = 400
            )
        ),
        enabled = true,
        capture = object : Painter() {
            override val intrinsicSize = Size(200F, 400F)
            override fun DrawScope.onDraw() {
                drawRect(Color.Gray)
            }
        },
        busy = false
    )
) {
    ScanCaptureScreen()
}
