package com.micrantha.skouter.ui.scan.preview

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenMappedModel

class ScanScreenModel(
    context: ScreenContext,
    environment: ScanEnvironment
) : ScreenMappedModel<ScanState, ScanUiState>(
    context,
    ScanState(),
    environment::map
) {
    init {
        store.addReducer(environment::reduce).applyEffect(environment::invoke)
    }
}
