package com.micrantha.skouter.ui.games.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.micrantha.bluebell.ui.scaffold.ScaffoldAction.Companion.scaffolding
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenMappedModel
import com.micrantha.skouter.ui.components.Strings.GamesTitle
import com.micrantha.skouter.ui.games.list.GameListActions.Load
import com.micrantha.skouter.ui.navi.NavAction

class GameListScreenModel(
    viewContext: ScreenContext,
    environment: GameListEnvironment,
    initialState: GameListState = GameListState()
) : ScreenMappedModel<GameListState, GameListUiState>(
    viewContext,
    initialState,
    environment::map
) {
    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)

        onActive()
    }

    private fun onActive() {
        dispatch(scaffolding {
            title = i18n.string(GamesTitle)
            action(NavAction(
                icon = Icons.Default.Add,
                action = {
                    dispatch(GameListActions.NewGame)
                }
            ))
        })
        dispatch(Load)
    }
}
