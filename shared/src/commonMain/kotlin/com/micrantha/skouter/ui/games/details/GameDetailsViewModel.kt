package com.micrantha.skouter.ui.games.details

import com.micrantha.bluebell.ui.view.MappedViewModel
import com.micrantha.bluebell.ui.view.ViewContext

class GameDetailsViewModel(
    viewContext: ViewContext,
    gameId: String,
    environment: GameDetailsEnvironment
) : MappedViewModel<GameDetailsState, GameDetailsUiState>(
    viewContext,
    initialState = GameDetailsState(),
    mapper = GameDetailsState::asUiState
) {
    init {
        store.addReducer(GameDetailsReducer(this))
            .applyEffect(GameDetailsEffects(environment))

        dispatch(GameDetailsAction.Load(gameId))
    }
}
