package com.micrantha.skouter.ui.games.list

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.ext.busy
import com.micrantha.bluebell.domain.ext.failure
import com.micrantha.bluebell.domain.ext.status
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.get
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.ui.components.Strings.LoadingGames
import com.micrantha.skouter.ui.components.toi18n
import com.micrantha.skouter.ui.games.action.GameAction.Failure
import com.micrantha.skouter.ui.games.action.GameAction.GameClicked
import com.micrantha.skouter.ui.games.create.GameCreateScreen
import com.micrantha.skouter.ui.games.details.GameDetailsScreen
import com.micrantha.skouter.ui.games.list.GameListAction.Load
import com.micrantha.skouter.ui.games.list.GameListAction.Loaded
import com.micrantha.skouter.ui.games.list.GameListAction.NewGame

class GameListEnvironment(
    private val context: ScreenContext,
    private val dispatcher: Dispatcher,
    private val localizedRepository: LocalizedRepository,
    private val repository: GameRepository,
) : Reducer<GameListState>, Effect<GameListState>,
    LocalizedRepository by localizedRepository, Router by context, Dispatcher by dispatcher {
    fun map(state: GameListState) = GameListUiState(
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
            is NewGame -> navigate<GameCreateScreen>(context.get())
            is GameClicked -> navigate<GameDetailsScreen>(context.get(action.arg))
        }
    }
}
