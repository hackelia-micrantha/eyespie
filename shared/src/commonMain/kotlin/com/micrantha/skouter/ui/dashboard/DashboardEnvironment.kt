package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.model.UiResult.Busy
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.domain.repository.PlayerRepository
import com.micrantha.skouter.domain.repository.ThingsRepository
import com.micrantha.skouter.ui.dashboard.DashboardAction.Load
import com.micrantha.skouter.ui.dashboard.DashboardAction.Loaded
import com.micrantha.skouter.ui.dashboard.DashboardUiState.Tabs

class DashboardEnvironment(
    private val context: ScreenContext,
    private val gameRepository: GameRepository,
    private val thingsRepository: ThingsRepository,
    private val playerRepository: PlayerRepository
) : Reducer<DashboardState>, Effect<DashboardState>, Dispatcher by context.dispatcher {
    fun map(state: DashboardState) = DashboardUiState(
        status = if (state.games != null && state.things != null && state.players != null)
            Ready(Tabs(state.games, state.players, state.things))
        else Busy()
    )

    override fun reduce(state: DashboardState, action: Action): DashboardState {
        return when (action) {
            else -> state
        }
    }

    override suspend fun invoke(action: Action, state: DashboardState) {
        when (action) {
            is Load -> dispatch(Loaded)
        }
    }
}
