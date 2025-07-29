package com.micrantha.eyespie.features.scan.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import com.micrantha.eyespie.core.ui.component.Choice
import com.micrantha.eyespie.domain.entities.Clues
import com.micrantha.eyespie.domain.entities.ColorClue
import com.micrantha.eyespie.domain.entities.Embedding
import com.micrantha.eyespie.domain.entities.LabelClue
import com.micrantha.eyespie.domain.entities.Location
import com.micrantha.eyespie.domain.entities.LocationClue
import com.micrantha.eyespie.domain.entities.Proof
import com.micrantha.eyespie.ui.PreviewContext
import okio.Path.Companion.toOkioPath
import java.nio.file.Paths

@Preview(showBackground = true, backgroundColor = 0xFF, widthDp = 200, heightDp = 400)
@Composable
fun ScanEditPreview() = PreviewContext(
    state = ScanEditUiState(
        image = object : Painter() {
            override val intrinsicSize = Size(200F, 400F)
            override fun DrawScope.onDraw() {
                drawRect(Color.Yellow)
            }
        },
        name = "Test",
        labels = listOf(
            Choice("label1", tag = "label1", key = "label1"),
            Choice("label2", tag = "label2", key = "label2"),
        ),
        colors = listOf(
            Choice("color1", tag = "color1", key = "color1"),
            Choice("color2", tag = "color2", key = "color2"),
        ),
        enabled = true,
        customLabel = "customLabel",
        customColor = null
    )
) {
    ScanEditScreen(it, Proof(
        clues = Clues(
            labels = List(3) { LabelClue("label$it", 0.5f * it) }.toSet(),
            colors = List(3) { ColorClue("color$it")}.toSet(),
            location = LocationClue(
                Location.Data(
                    name = "somewhere",
                    city = "somewhere",
                    region = "somewhere",
                    country = "somewhere",
                    accuracy = 0.5f
                )
            )
        ),
        location = Location.Point(
            180.0, 280.0
        ),
        match = Embedding.EMPTY,
        image = Paths.get(".").toOkioPath(),
        name = "abc",
        playerID = "123"
    ))
}