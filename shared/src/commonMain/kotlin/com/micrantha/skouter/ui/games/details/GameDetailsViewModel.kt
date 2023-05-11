package com.micrantha.skouter.ui.games.details

import com.micrantha.bluebell.ui.view.MappedViewModel
import com.micrantha.bluebell.ui.view.ViewContext

class GameDetailsViewModel(
    viewContext: ViewContext,
    private val gameId: String,
    environment: GameDetailsEnvironment
) : MappedViewModel<GameDetailsState, GameDetailsUiState>(
    viewContext = viewContext,
    initialState = GameDetailsState(),
    mapper = GameDetailsState::asUiState
) {
    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }

    override fun onScreenActive() {
        dispatch(GameDetailsAction.Load(gameId))
    }
}
