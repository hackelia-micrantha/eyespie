package com.micrantha.eyespie.features.game.ui.list

import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.arch.Effect
import com.micrantha.bluebell.arch.Reducer
import com.micrantha.bluebell.arch.StateMapper
import com.micrantha.bluebell.ext.busy
import com.micrantha.bluebell.ext.failure
import com.micrantha.bluebell.ext.status
import com.micrantha.bluebell.domain.repository.LocalizedRepository
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.navigate
import com.micrantha.eyespie.app.Strings.LoadingGames
import com.micrantha.eyespie.core.ui.component.toi18n
import com.micrantha.eyespie.domain.repository.GameRepository
import com.micrantha.eyespie.features.game.ui.component.GameAction.Failure
import com.micrantha.eyespie.features.game.ui.component.GameAction.GameClicked
import com.micrantha.eyespie.features.game.ui.create.GameCreateScreen
import com.micrantha.eyespie.features.game.ui.detail.GameDetailScreenArg
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsScreen
import com.micrantha.eyespie.features.game.ui.list.GameListAction.Load
import com.micrantha.eyespie.features.game.ui.list.GameListAction.Loaded
import com.micrantha.eyespie.features.game.ui.list.GameListAction.NewGame

class GameListEnvironment(
    private val context: ScreenContext,
    private val repository: GameRepository,
) : Reducer<GameListState>, Effect<GameListState>,
    StateMapper<GameListState, GameListUiState>,
    LocalizedRepository by context.i18n, Router by context.router,
    Dispatcher by context.dispatcher {

    override fun map(state: GameListState) = GameListUiState(
        status = state.status
    )

    override fun reduce(state: GameListState, action: Action) = when (action) {
        is Load ->
            state.copy(status = busy(LoadingGames))

        is Loaded -> state.copy(
            status = action.data.status()
        )

        is Failure -> state.copy(status = failure(action.error.toi18n()))
        else -> state
    }

    override suspend fun invoke(action: Action, state: GameListState) {
        when (action) {
            is Load -> repository.games()
                .onFailure { dispatch(Failure(it)) }
                .onSuccess { dispatch(Loaded(it)) }

            is NewGame -> context.navigate<GameCreateScreen>()
            is GameClicked -> context.navigate<GameDetailsScreen, GameDetailScreenArg>(arg = action.arg)
        }
    }
}
