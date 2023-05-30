package com.micrantha.skouter.ui.scan.preview

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class ScanScreenModel(
    context: ScreenContext,
    environment: ScanEnvironment
) : MappedScreenModel<ScanState, ScanUiState>(
    context,
    ScanState(),
    environment::map
) {
    init {
        store.addReducer(environment::reduce).applyEffect(environment::invoke)
    }
}
