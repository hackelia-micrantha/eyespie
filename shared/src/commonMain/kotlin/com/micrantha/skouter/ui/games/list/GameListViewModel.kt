package com.micrantha.skouter.ui.games.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.micrantha.bluebell.ui.scaffold.ScaffoldAction
import com.micrantha.bluebell.ui.view.MappedViewModel
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.components.i18n
import com.micrantha.skouter.ui.games.list.GameListActions.Load
import com.micrantha.skouter.ui.navi.NavAction

class GameListViewModel(
    viewContext: ViewContext,
    environment: GameListEnvironment,
) : MappedViewModel<GameListState, GameListUiState>(
    viewContext,
    GameListState(),
    GameListState::asUiState
) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }

    override fun onScreenActive() {
        dispatch(ScaffoldAction.navigation {
            title = string(i18n.GamesTitle)
            action(NavAction(
                icon = Icons.Default.Add,
                action = {
                    dispatch(GameListActions.NewGame)
                }
            ))
        })
        dispatch(Load)
    }

    override fun onScreenIdle() {
        dispatch(ScaffoldAction.Reset)
    }
}
