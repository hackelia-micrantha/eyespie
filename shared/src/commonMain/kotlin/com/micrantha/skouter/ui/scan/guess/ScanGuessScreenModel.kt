package com.micrantha.skouter.ui.scan.guess

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import org.kodein.di.instance

class ScanGuessScreenModel(
    args: ScanGuessArgs,
    context: ScreenContext,
    mapper: ScanGuessMapper,
) : MappedScreenModel<ScanGuessState, ScanGuessUiState>(
    context = context,
    initialState = ScanGuessState(),
    mapper = mapper::invoke
) {
    private val environment: ScanGuessEnvironment by di.instance(arg = args)

    init {
        store.addReducer(environment::reduce).applyEffect(environment::invoke)
    }

}
