package com.micrantha.skouter.ui.scan.edit

import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.ui.component.Choice
import okio.Path

data class ScanEditState(
    val labels: MutableMap<String, LabelClue>? = null,
    val customLabel: String? = null,
    val colors: MutableMap<String, ColorClue>? = null,
    val name: String? = null,
    val path: Path? = null,
    val image: Painter? = null,
)

data class ScanEditUiState(
    val labels: List<Choice>,
    val customLabel: String?,
    val colors: List<Choice>,
    val name: String,
    val image: Painter?
)

data class ScanEditArg(
    val clues: Clues,
    val path: Path
)

sealed class ScanEditAction : Action {
    data class Init(val arg: ScanEditArg) : ScanEditAction()

    data class LabelChanged(val data: Choice) : ScanEditAction()
    data class ColorChanged(val data: Choice) : ScanEditAction()

    data class CustomLabelChanged(val data: String) : ScanEditAction()

    object SaveScanEdit : ScanEditAction()

    data class LoadedImage(val data: Painter) : ScanEditAction()

    data class NameChanged(val data: String) : ScanEditAction()
}


fun ScanEditState.asNewThing() = Thing.Create(
    clues = Clues(
        labels = labels?.values?.toSet(),
        colors = colors?.values?.toSet()
    ),
    name = name!!,
    path = path!!
)
