package com.micrantha.eyespie.features.dashboard.ui

import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.arch.Effect
import com.micrantha.bluebell.arch.Reducer
import com.micrantha.bluebell.arch.StateMapper
import com.micrantha.bluebell.ui.model.Ready
import com.micrantha.bluebell.ui.model.UiResult.Busy
import com.micrantha.bluebell.ui.model.UiResult.Failure
import com.micrantha.bluebell.ui.model.isReady
import com.micrantha.bluebell.ui.model.map
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.navigate
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.GuessThing
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.Load
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.LoadError
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.Loaded
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.ScanNewThing
import com.micrantha.eyespie.features.dashboard.ui.DashboardUiState.Data
import com.micrantha.eyespie.features.dashboard.ui.DashboardUiState.Data.Nearby
import com.micrantha.eyespie.features.dashboard.ui.DashboardUiState.Data.TabContent
import com.micrantha.eyespie.features.dashboard.ui.usecase.DashboardLoadUseCase
import com.micrantha.eyespie.features.game.ui.component.GameAction
import com.micrantha.eyespie.features.game.ui.detail.GameDetailScreenArg
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsScreen
import com.micrantha.eyespie.features.guess.ui.ScanGuessScreen
import com.micrantha.eyespie.features.scan.ui.capture.ScanCaptureScreen
import eyespie.composeapp.generated.resources.loading_dashboard
import eyespie.composeapp.generated.resources.network_failure
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DashboardEnvironment(
    private val context: ScreenContext,
    private val dashboardLoadUseCase: DashboardLoadUseCase
) : Reducer<DashboardState>, Effect<DashboardState>, Dispatcher by context.dispatcher {

    companion object : StateMapper<DashboardState, DashboardUiState> {
        override fun map(state: DashboardState) = DashboardUiState(
            status = state.status.map {
                Data(
                    nearby = Nearby(
                        players = TabContent(
                            data = state.players ?: emptyList(),
                            hasMore = false
                        ),
                        things = TabContent(
                            state.things ?: emptyList(),
                            hasMore = false
                        ),
                    ),
                    friends = TabContent(
                        data = state.friends ?: emptyList(),
                        hasMore = false
                    )
                )
            }
        )
    }

    override fun reduce(state: DashboardState, action: Action): DashboardState {
        return when (action) {
            is Loaded -> state.copy(
                status = Ready(),
                things = action.things,
                players = action.players,
                friends = action.friends
            )

            is LoadError -> state.copy(
                status = Failure(S.network_failure)
            )

            is Load -> if (state.status.isReady) state else state.copy(
                status = Busy(S.loading_dashboard)
            )

            else -> state
        }
    }

    override suspend fun invoke(action: Action, state: DashboardState) {
        when (action) {
            is GameAction.GameClicked -> context.navigate<GameDetailsScreen, GameDetailScreenArg>(
                arg = action.arg
            )

            is Load ->
                dashboardLoadUseCase().onEach { res ->
                    res.onSuccess { dispatch(it) }
                        .onFailure { dispatch(LoadError) }
                }.launchIn(dispatchScope)

            is ScanNewThing -> context.navigate<ScanCaptureScreen>()

            is GuessThing -> context.router.navigate(
                ScanGuessScreen(
                    action.thing.id
                )
            )
        }
    }
}
