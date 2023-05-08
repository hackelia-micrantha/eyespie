package com.micrantha.skouter.ui.games.list

import com.micrantha.bluebell.ui.view.MappedViewModel
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.MainAction.SetTitle
import com.micrantha.skouter.ui.arch.i18n
import com.micrantha.skouter.ui.games.list.GameListActions.Load

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
        store.addReducer(GameListReducer(viewContext))
            .applyEffect(GameListEffects(viewContext, environment))

        dispatch(SetTitle(string(i18n.GamesTitle)))
        dispatch(Load)

    }
}
