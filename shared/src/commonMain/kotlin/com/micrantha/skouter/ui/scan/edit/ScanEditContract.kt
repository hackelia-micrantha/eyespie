package com.micrantha.skouter.ui.scan.edit

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.ui.component.Choice
import okio.Path

data class ScanEditState(
    val labels: MutableMap<String, LabelClue>? = null,
    val customLabel: String? = null,
    val name: String? = null,
    val path: Path? = null
)

data class ScanEditUiState(
    val labels: List<Choice>,
    val customLabel: String?,
    val name: String
)

data class ScanEditArg(
    val proof: Proof,
    val path: Path
)

sealed class ScanEditAction : Action {
    data class Init(val arg: ScanEditArg) : ScanEditAction()

    data class LabelChanged(val data: Choice) : ScanEditAction()

    data class CustomLabelChanged(val data: String) : ScanEditAction()

    object SaveScanEdit : ScanEditAction()

    data class NameChanged(val data: String) : ScanEditAction()
}


fun ScanEditState.asNewThing() = Thing.Create(
    proof = Proof(
        labels = labels?.values?.toSet()
    ),
    name = name!!,
    path = path!!
)
