package com.micrantha.skouter.ui.scan.edit

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.ui.screen.MappedScreenEnvironment

class ScanEditEnvironment : MappedScreenEnvironment<ScanEditState, ScanEditUiState> {
    override fun reduce(state: ScanEditState, action: Action) = when (action) {
        else -> state
    }

    override suspend fun invoke(action: Action, state: ScanEditState) {
        when (action) {
        }
    }

    override fun map(state: ScanEditState) = ScanEditUiState(
        
    )

}