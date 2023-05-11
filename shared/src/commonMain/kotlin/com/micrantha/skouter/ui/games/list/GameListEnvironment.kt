package com.micrantha.skouter.ui.games.list

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.busy
import com.micrantha.bluebell.domain.model.error
import com.micrantha.bluebell.domain.model.status
import com.micrantha.bluebell.ui.navi.Router
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.ui.components.i18n.LoadingGames
import com.micrantha.skouter.ui.components.toi18n
import com.micrantha.skouter.ui.games.list.GameListActions.Error
import com.micrantha.skouter.ui.games.list.GameListActions.GameClicked
import com.micrantha.skouter.ui.games.list.GameListActions.Load
import com.micrantha.skouter.ui.games.list.GameListActions.Loaded
import com.micrantha.skouter.ui.games.list.GameListActions.NewGame
import com.micrantha.skouter.ui.navi.NavContext
import com.micrantha.skouter.ui.navi.Routes

class GameListEnvironment(
    private val router: Router,
    private val dispatcher: Dispatcher,
    private val localizedRepository: LocalizedRepository,
    private val repository: GameRepository,
) : GameRepository by repository, Reducer<GameListState>, Effect<GameListState>,
    LocalizedRepository by localizedRepository, Router by router, Dispatcher by dispatcher {

    override fun reduce(state: GameListState, action: Action) = when (action) {
        is Load ->
            state.copy(status = busy(LoadingGames))
        is Loaded -> state.copy(
            status = action.data.status()
        )
        is Error -> state.copy(status = error(action.error.toi18n()))
        else -> state
    }

    override suspend fun invoke(action: Action, state: GameListState) {
        when (action) {
            is Load -> repository.games()
                .onFailure { dispatch(Error(it)) }
                .onSuccess { dispatch(Loaded(it)) }
            is NewGame -> changeNavigationContext(NavContext.CreateGame)
            is GameClicked -> navigate(Routes.GameDetails, action.id)
        }
    }
}
