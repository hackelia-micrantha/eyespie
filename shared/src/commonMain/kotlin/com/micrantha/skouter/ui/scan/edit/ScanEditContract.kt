package com.micrantha.skouter.ui.scan.edit

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.ui.component.Choice

data class ScanEditState(
    val labels: List<LabelClue>? = null,
)

data class ScanEditUiState(
    val labels: List<Choice>
)

sealed class ScanEditAction : Action {
    data class Init(val proof: Proof) : ScanEditAction()

    data class LabelChanged(val data: Choice) : ScanEditAction()

}
