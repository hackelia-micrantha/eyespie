package com.micrantha.eyespie.ui.game.detail

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class GameDetailsScreenModel(
    screenContext: ScreenContext,
    environment: GameDetailsEnvironment,
    initialState: GameDetailsState = GameDetailsState(),
) : MappedScreenModel<GameDetailsState, GameDetailsUiState>(
    context = screenContext,
    initialState = initialState,
    mapper = environment::map
) {
    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
