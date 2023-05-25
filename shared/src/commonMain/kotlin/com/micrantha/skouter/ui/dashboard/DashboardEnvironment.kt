package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.ext.busy
import com.micrantha.bluebell.domain.ext.failure
import com.micrantha.bluebell.domain.model.map
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.skouter.domain.models.GameList
import com.micrantha.skouter.domain.models.Location
import com.micrantha.skouter.domain.models.PlayerList
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.domain.repository.PlayerRepository
import com.micrantha.skouter.domain.repository.ThingsRepository
import com.micrantha.skouter.ui.components.S
import com.micrantha.skouter.ui.dashboard.DashboardAction.Load
import com.micrantha.skouter.ui.dashboard.DashboardAction.LoadError
import com.micrantha.skouter.ui.dashboard.DashboardAction.Loaded
import com.micrantha.skouter.ui.dashboard.DashboardUiState.Tabs
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class DashboardEnvironment(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
    private val gameRepository: GameRepository,
    private val thingsRepository: ThingsRepository,
    private val playerRepository: PlayerRepository
) : Reducer<DashboardState>, Effect<DashboardState>, Dispatcher by context.dispatcher {

    fun map(state: DashboardState) = DashboardUiState(
        status = state.status.map {
            Tabs(
                state.games ?: emptyList(),
                state.players ?: emptyList(),
                state.things ?: emptyList()
            )
        }
    )

    override fun reduce(state: DashboardState, action: Action): DashboardState {
        return when (action) {
            is Loaded -> state.copy(
                games = action.games,
                things = action.things,
                players = action.players
            )
            is LoadError -> state.copy(
                status = context.i18n.failure(S.NetworkFailure)
            )
            is Load -> state.copy(
                status = context.i18n.busy(S.LoadingDashboard)
            )
            else -> state
        }
    }

    override suspend fun invoke(action: Action, state: DashboardState) {
        when (action) {
            is Load -> {
                val thingFlow = thingsRepository.nearby(
                    accountRepository.currentPlayer!!.id,
                    Location.Point(49.319981, -123.072411)
                )
                val gameFlow = flow<GameList> { emit(emptyList()) }
                val playerFlow = flow<PlayerList> { emit(emptyList()) }
                combine(
                    flow = thingFlow,
                    flow2 = gameFlow,
                    flow3 = playerFlow
                ) { things, games, players ->
                    Loaded(things, games, players)
                }.catch {
                    Napier.e("dashboard", it)
                    dispatch(LoadError)
                }.collect {
                    dispatch(it)
                }
            }
        }
    }
}
