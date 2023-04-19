package com.micrantha.skouter.presentation.screens.games

import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.namegame.ui.view.mapIn
import com.micrantha.skouter.presentation.view.ViewModel
import com.micrantha.skouter.AppRoutes
import com.micrantha.skouter.presentation.view.MappedViewModel

class GameListViewModel(
    viewContext: ViewContext,
    initialState: GameListState = GameListState()
) : MappedViewModel<GameListState, GameListUiState>(viewContext, initialState, ::mapper) {

    init {
        store.applyEffect { action, _ ->
            when(action) {
                is GameListActions.NewGame -> viewContext.router.navigate(AppRoutes.NewGame)
            }
        }
    }
}
