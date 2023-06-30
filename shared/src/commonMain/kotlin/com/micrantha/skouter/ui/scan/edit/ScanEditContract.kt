package com.micrantha.skouter.ui.scan.edit

import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.ui.component.Choice

data class ScanEditState(
    val labels: MutableMap<String, LabelClue>? = null,
    val customLabel: String? = null,
    val colors: MutableMap<String, ColorClue>? = null,
    val customColor: String? = null,
    val name: String? = null,
    val image: Painter? = null,
    val proof: Proof? = null,
    val disabled: Boolean = false,
)

data class ScanEditUiState(
    val labels: List<Choice>,
    val customLabel: String?,
    val colors: List<Choice>,
    val customColor: String?,
    val name: String,
    val image: Painter?,
    val enabled: Boolean
)

sealed class ScanEditAction : Action {
    data class Init(val proof: Proof) : ScanEditAction()

    data class LabelChanged(val data: Choice) : ScanEditAction()
    data class ColorChanged(val data: Choice) : ScanEditAction()

    data class CustomLabelChanged(val data: String) : ScanEditAction()

    object SaveScanEdit : ScanEditAction()

    object SaveThingError : ScanEditAction()

    object LoadError : ScanEditAction()

    data class LoadedImage(val data: Painter) : ScanEditAction()

    data class NameChanged(val data: String) : ScanEditAction()

    object ClearColor : ScanEditAction()

    object ClearLabel : ScanEditAction()
}
