package com.micrantha.skouter.ui.scan.edit

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenMappedModel

class ScanEditViewModel(
    context: ScreenContext,
    environment: ScanEditEnvironment
) : ScreenMappedModel<ScanEditState, ScanEditUiState>(
    context,
    ScanEditState(),
    environment::map
) {
    init {
        store.addReducer(environment::reduce).applyEffect(environment)
    }
}
