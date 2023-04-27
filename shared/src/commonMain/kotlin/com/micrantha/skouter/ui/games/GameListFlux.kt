package com.micrantha.skouter.ui.games

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
import com.micrantha.skouter.ui.navi.Routes

sealed class GameListActions : Action {
    object NewGame : GameListActions()
    object Load : GameListActions()
    data class OnLoaded(val data: List<GameListing>) : GameListActions()
    data class OnError(val error: Throwable) : GameListActions()
}

class GameListReducer(private val i18n: LocalizedRepository) : Reducer<GameListState> {
    override fun invoke(state: GameListState, action: Action) =
        when (action) {
            is GameListActions.Load ->
                state.copy(status = i18n.busy(LoadingGames))
            is GameListActions.OnLoaded -> state.copy(
                games = action.data,
                status = action.data.status()
            )
            is GameListActions.OnError -> state.copy(status = i18n.error(action.error.toi18n()))
            else -> state
        }
}

class GameListEffects(
    private val viewContext: ViewContext,
    private val environment: GameListEnvironment
) : Effect<GameListState>, Dispatcher by viewContext, Router by viewContext {
    override suspend fun invoke(action: Action, state: GameListState) {
        when (action) {
            is GameListActions.Load -> environment.games()
                .onFailure { dispatch(GameListActions.OnError(it)) }
                .onSuccess { dispatch(GameListActions.OnLoaded(it)) }
            is GameListActions.NewGame -> navigate(Routes.NewGame)
        }
    }
}
