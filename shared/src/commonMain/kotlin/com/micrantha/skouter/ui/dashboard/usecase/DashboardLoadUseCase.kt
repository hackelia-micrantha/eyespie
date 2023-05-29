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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

class DashboardLoadUseCase(
    private val thingsRepository: ThingsRepository,
    private val playerRepository: PlayerRepository,
    private val currentSession: CurrentSession
) {
    suspend operator fun invoke(): Flow<DashboardAction> = try {
        val lastLocation = currentSession.requirePlayer().location
        val playerID = currentSession.requirePlayer().id

        val thingFlow = flow {
            val res = if (lastLocation != null) {
                thingsRepository.nearby(location = lastLocation.point)
            } else {
                thingsRepository.things(playerID)
            }
            res.onSuccess { emit(it) }
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
        combine(
            flow = thingFlow,
            flow2 = friendFlow,
            flow3 = playerFlow
        )
        { things, friends, players ->
            Loaded(things, friends, players)
        }.catch {
            Napier.e("dashboard", it)
            throw it
        }
    } catch (err: Throwable) {
        emptyFlow()
    }
}
