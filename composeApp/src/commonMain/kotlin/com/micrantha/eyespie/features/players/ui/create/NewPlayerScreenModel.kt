package com.micrantha.eyespie.features.players.ui.create

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class NewPlayerScreenModel(
    context: ScreenContext,
    environment: NewPlayerEnvironment,
    initialState: NewPlayerState = NewPlayerState()
) : MappedScreenModel<NewPlayerState, NewPlayerUiState>(
    context = context,
    initialState = initialState,
    mapper = environment::map
) {
    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}