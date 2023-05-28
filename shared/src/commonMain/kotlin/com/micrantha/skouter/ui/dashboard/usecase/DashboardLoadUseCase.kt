package com.micrantha.skouter.ui.dashboard.usecase

import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.domain.repository.PlayerRepository
import com.micrantha.skouter.domain.repository.ThingsRepository
import com.micrantha.skouter.ui.dashboard.DashboardAction
import com.micrantha.skouter.ui.dashboard.DashboardAction.Loaded
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class DashboardLoadUseCase(
    private val thingsRepository: ThingsRepository,
    private val playerRepository: PlayerRepository,
    private val currentSession: CurrentSession
) {
    operator fun invoke(): Flow<DashboardAction> {
        val thingFlow = flow {
            thingsRepository.things(currentSession.requirePlayer().id)
                .onSuccess { emit(it) }
                .onFailure { throw it }
        }
        val friendFlow = flow {
            playerRepository.players()
                .onSuccess { emit(it) }
                .onFailure { throw it }
        }
        val playerFlow = flow {
            playerRepository.players()
                .onSuccess { emit(it) }
                .onFailure { throw it }
        }
        return combine(
            flow = thingFlow,
            flow2 = friendFlow,
            flow3 = playerFlow
        ) { things, friends, players ->
            Loaded(things, friends, players)
        }.catch {
            Napier.e("dashboard", it)
            throw it
        }
    }
}
