package com.micrantha.skouter.ui.games

import com.micrantha.bluebell.ui.view.MappedViewModel
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.MainAction.SetTitle
import com.micrantha.skouter.ui.arch.i18n
import com.micrantha.skouter.ui.games.GameListActions.Load
import kotlinx.coroutines.launch

class GameListViewModel(
    viewContext: ViewContext,
    environment: GameListEnvironment,
    initialState: GameListState = GameListState()
) : MappedViewModel<GameListState, GameListUiState>(
    viewContext,
    initialState,
    GameListState::asUiState
) {

    init {

        viewModelScope.launch {
            environment.test()
        }
        store.addReducer(GameListReducer(viewContext))
            .applyEffect(GameListEffects(viewContext, environment))

        dispatch(
            SetTitle(resource(i18n.GamesTitle))
        )
        dispatch(Load)

    }
}
