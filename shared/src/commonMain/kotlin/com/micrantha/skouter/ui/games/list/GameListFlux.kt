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
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.domain.models.GameListing
import com.micrantha.skouter.ui.arch.i18n.LoadingGames
import com.micrantha.skouter.ui.arch.toi18n
import com.micrantha.skouter.ui.games.list.GameListActions.Error
import com.micrantha.skouter.ui.games.list.GameListActions.GameClicked
import com.micrantha.skouter.ui.games.list.GameListActions.Load
import com.micrantha.skouter.ui.games.list.GameListActions.Loaded
import com.micrantha.skouter.ui.games.list.GameListActions.NewGame
import com.micrantha.skouter.ui.navi.Routes

sealed class GameListActions : Action {
    object NewGame : GameListActions()
    object Load : GameListActions()
    data class Loaded(val data: List<GameListing>) : GameListActions()
    data class Error(val error: Throwable) : GameListActions()

    data class GameClicked(val id: String) : GameListActions()
}

class GameListReducer(private val i18n: LocalizedRepository) : Reducer<GameListState> {
    override fun invoke(state: GameListState, action: Action) =
        when (action) {
            is Load ->
                state.copy(status = i18n.busy(LoadingGames))
            is Loaded -> state.copy(
                status = action.data.status()
            )
            is Error -> state.copy(status = i18n.error(action.error.toi18n()))
            else -> state
        }
}

class GameListEffects(
    private val viewContext: ViewContext,
    private val environment: GameListEnvironment
) : Effect<GameListState>, Dispatcher by viewContext, Router by viewContext {
    override suspend fun invoke(action: Action, state: GameListState) {
        when (action) {
            is Load -> environment.games()
                .onFailure { dispatch(Error(it)) }
                .onSuccess { dispatch(Loaded(it)) }
            is NewGame -> navigate(Routes.NewGame)
            is GameClicked -> navigate(Routes.GameDetails, action.id)
        }
    }
}
