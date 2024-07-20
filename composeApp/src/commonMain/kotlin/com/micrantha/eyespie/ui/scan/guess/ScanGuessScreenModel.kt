package com.micrantha.eyespie.ui.scan.guess

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.eyespie.ui.scan.guess.ScanGuessAction.Load
import org.kodein.di.instance
import org.kodein.di.on

class ScanGuessScreenModel(
    args: ScanGuessArgs,
    context: ScreenContext,
    mapper: ScanGuessMapper,
) : MappedScreenModel<ScanGuessState, ScanGuessUiState>(
    context = context,
    initialState = ScanGuessState(),
    mapper = mapper::invoke
) {
    private val environment: ScanGuessEnvironment by di.on(this).instance(arg = args)

    init {
        store.addReducer(environment::reduce).applyEffect(environment::invoke)

        dispatch(Load)
    }
}
