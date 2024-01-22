package com.micrantha.skouter.ui.scan.capture

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import org.kodein.di.instance
import org.kodein.di.on

class ScanCaptureScreenModel(
    context: ScreenContext,
    mapper: ScanCaptureStateMapper,
) : MappedScreenModel<ScanState, ScanUiState>(
    context,
    ScanState(),
    mapper::map
) {
    private val environment by di.on(this).instance<ScanCaptureEnvironment>()

    init {
        store.addReducer(environment::reduce).applyEffect(environment::invoke)
    }
}
