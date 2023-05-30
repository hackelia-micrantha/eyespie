package com.micrantha.skouter.ui.game.detail

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class GameDetailsScreenModel(
    private val arg: GameDetailScreenArg,
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

        onActive()
    }

    private fun onActive() {

        dispatch(GameDetailsAction.Load(arg))
    }
}
