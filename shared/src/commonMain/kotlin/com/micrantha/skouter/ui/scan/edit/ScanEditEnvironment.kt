package com.micrantha.skouter.ui.scan.edit

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.ui.screen.MappedScreenEnvironment
import com.micrantha.skouter.ui.component.Choice
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.Init

class ScanEditEnvironment : MappedScreenEnvironment<ScanEditState, ScanEditUiState> {
    override fun reduce(state: ScanEditState, action: Action) = when (action) {
        is Init -> state.copy(labels = action.proof.labels)
        else -> state
    }

    override suspend fun invoke(action: Action, state: ScanEditState) {
        when (action) {
        }
    }

    override fun map(state: ScanEditState) = ScanEditUiState(
        labels = state.labels?.map { Choice(it.data, it.points.toString()) } ?: emptyList()
    )

}
