package com.micrantha.eyespie.features.scan.ui.capture

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import org.kodein.di.instance
import org.kodein.di.on

class ScanCaptureScreenModel(
    context: ScreenContext,
    environment: ScanCaptureEnvironment,
    mapper: ScanCaptureStateMapper,
) : MappedScreenModel<ScanState, ScanUiState>(
    context,
    ScanState(),
    mapper::map
) {
    init {
        store.addReducer(environment::reduce).applyEffect(environment::invoke)
    }
}
