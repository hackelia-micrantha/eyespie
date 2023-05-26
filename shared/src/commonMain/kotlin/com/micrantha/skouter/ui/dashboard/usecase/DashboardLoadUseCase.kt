package com.micrantha.skouter.ui.dashboard.usecase

import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.domain.repository.PlayerRepository
import com.micrantha.skouter.domain.repository.ThingsRepository
import com.micrantha.skouter.ui.dashboard.DashboardAction
import com.micrantha.skouter.ui.dashboard.DashboardAction.LoadError
import com.micrantha.skouter.ui.dashboard.DashboardAction.Loaded
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class DashboardLoadUseCase(
    private val thingsRepository: ThingsRepository,
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<DashboardAction> {
        val thingFlow = flow {
            thingsRepository.things(accountRepository.currentPlayer!!.id)
                .onSuccess { emit(it) }
                .onFailure { throw it }
        }
        val gameFlow = flow {
            gameRepository.games()
                .onSuccess { emit(it) }.onFailure { throw it }
        }
        val playerFlow = flow {
            playerRepository.players()
                .onSuccess { emit(it) }
                .onFailure { throw it }
        }
        return combine(
            flow = thingFlow,
            flow2 = gameFlow,
            flow3 = playerFlow
        ) { things, games, players ->
            Loaded(things, games, players)
        }.catch {
            Napier.e("dashboard", it)
            LoadError
        }
    }
}
