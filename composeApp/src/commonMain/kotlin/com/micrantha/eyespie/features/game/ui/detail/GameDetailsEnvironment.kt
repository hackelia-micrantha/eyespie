package com.micrantha.eyespie.features.game.ui.detail

import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.arch.Effect
import com.micrantha.bluebell.arch.Reducer
import com.micrantha.bluebell.arch.StateMapper
import com.micrantha.bluebell.ext.busy
import com.micrantha.bluebell.ext.failure
import com.micrantha.bluebell.domain.repository.LocalizedRepository
import com.micrantha.bluebell.ui.model.Ready
import com.micrantha.bluebell.ui.model.mapNotNull
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.eyespie.app.Strings.LoadingGame
import com.micrantha.eyespie.core.ui.component.toi18n
import com.micrantha.eyespie.domain.repository.GameRepository
import com.micrantha.eyespie.features.game.ui.component.GameAction.Failure
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsAction.Load
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsAction.Loaded

class GameDetailsEnvironment(
    private val context: ScreenContext,
    private val gameRepository: GameRepository,
) : Reducer<GameDetailsState>, Effect<GameDetailsState>,
    StateMapper<GameDetailsState, GameDetailsUiState>,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n {

    override fun map(state: GameDetailsState) = GameDetailsUiState(
        status = state.status.mapNotNull { state.game }
    )

    override fun reduce(state: GameDetailsState, action: Action) = when (action) {
        is Load -> state.copy(status = busy(LoadingGame))
        is Loaded -> state.copy(
            status = Ready(),
            game = action.game,
        )

        is Failure -> state.copy(status = failure(action.error.toi18n()))
        else -> state
    }

    override suspend fun invoke(action: Action, state: GameDetailsState) {
        when (action) {
            is Load -> {
                gameRepository.game(action.id).onFailure {
                    dispatch(Failure(it))
                }.onSuccess { game ->
                    dispatch(Loaded(game))
                }
            }
        }
    }
}
