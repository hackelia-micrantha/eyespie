package com.micrantha.skouter.ui.scan.edit

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class ScanEditScreenModel(
    context: ScreenContext,
    environment: ScanEditEnvironment
) : MappedScreenModel<ScanEditState, ScanEditUiState>(
    context,
    ScanEditState(),
    ScanEditEnvironment::map
) {
    init {
        store.addReducer(environment::reduce).applyEffect(environment)
    }
}
