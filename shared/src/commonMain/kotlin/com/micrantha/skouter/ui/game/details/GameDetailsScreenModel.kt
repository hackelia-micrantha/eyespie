package com.micrantha.skouter.ui.games.details

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenMappedModel

class GameDetailsScreenModel(
    private val arg: GameDetailScreenArg,
    screenContext: ScreenContext,
    environment: GameDetailsEnvironment,
    initialState: GameDetailsState = GameDetailsState(),
) : ScreenMappedModel<GameDetailsState, GameDetailsUiState>(
    screenContext = screenContext,
    initialState = initialState,
    mapper = environment::map
) {
    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)

        onActive()
    }

    private fun onActive() {

        dispatch(GameDetailsAction.Load(arg))
    }
}
