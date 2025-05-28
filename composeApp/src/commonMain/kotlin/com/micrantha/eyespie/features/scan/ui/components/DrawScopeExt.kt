package com.micrantha.eyespie.features.scan.ui.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import com.micrantha.eyespie.features.scan.ui.capture.ScanBox
import com.micrantha.eyespie.features.scan.ui.capture.ScanMask


@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawScanBox(
    data: ScanBox,
    measurer: TextMeasurer
) {
    val bounds = data.scale(size.width, size.height)

    val textSize = measurer.measure(data.label).size

    val textPos = bounds.topLeft

    drawOutline(
        outline = Outline.Rectangle(bounds),
        color = Color.Red.copy(alpha = 0.5f),
        style = Stroke(2f)
    )
    drawRoundRect(
        color = Color.Red,
        alpha = 0.5f,
        size = Size(textSize.width + 10f, textSize.height + 5f),
        cornerRadius = CornerRadius(1f),
        topLeft = textPos,
    )
    drawText(
        textMeasurer = measurer,
        text = data.label,
        topLeft = textPos.plus(Offset(3f, 0f)),
    )
}

fun DrawScope.drawScanMask(
    data: ScanMask
) {
    drawImage(data.mask)
}
