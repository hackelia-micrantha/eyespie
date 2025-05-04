package com.micrantha.eyespie.features.scan.ui.edit

import com.micrantha.bluebell.history.HistoryState
import com.micrantha.bluebell.history.historyEffectOf
import com.micrantha.bluebell.history.historyMapperOf
import com.micrantha.bluebell.history.historyReducerOf
import com.micrantha.bluebell.history.historyStateOf
import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class ScanEditScreenModel(
    context: ScreenContext,
    environment: ScanEditEnvironment
) : MappedScreenModel<HistoryState<ScanEditState>, ScanEditUiState>(
    context,
    historyStateOf(ScanEditState()),
    historyMapperOf(environment)
) {
    init {
        store.addReducer(historyReducerOf(environment))
            .applyEffect(historyEffectOf(environment))
    }
}
