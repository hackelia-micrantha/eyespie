package com.micrantha.skouter.ui.scan.view

import cafe.adriel.voyager.core.model.coroutineScope
import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.instance
import org.kodein.di.on

class ScanScreenModel(
    context: ScreenContext,
    mapper: ScanStateMapper,
) : MappedScreenModel<ScanState, ScanUiState>(
    context,
    ScanState(),
    mapper::map
) {
    private val environment by di.on(this)
        .instance<CoroutineScope, ScanEnvironment>(arg = coroutineScope)

    init {
        store.addReducer(environment::reduce).applyEffect(environment::invoke)
    }
}
