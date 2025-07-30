package com.micrantha.eyespie.features.dashboard.ui.usecase

import com.micrantha.bluebell.domain.usecase.flowUseCase
import com.micrantha.eyespie.core.data.account.model.CurrentSession
import com.micrantha.eyespie.domain.entities.Location.Point
import com.micrantha.eyespie.domain.repository.ThingRepository
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.Loaded
import com.micrantha.eyespie.features.players.domain.repository.PlayerRepository
import com.micrantha.eyespie.features.players.domain.entities.Player
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class DashboardLoadUseCase(
    private val thingsRepository: ThingRepository,
    private val playerRepository: PlayerRepository,
    private val currentSession: CurrentSession
) {
    operator fun invoke() = flowUseCase {
        val player = currentSession.requirePlayer()
        val location = player.location?.point

        combine(
            flow = if (location != null) things(location) else flow { emit(emptyList()) },
            flow2 = if (location != null) players(location) else flow { emit(emptyList()) },
            flow3 = friends,
        ) { nearbyThings, nearbyPlayers, friends ->
            Loaded(nearbyThings, nearbyPlayers, friends)
        }
    }

    private fun things(location: Point) = flow {
        thingsRepository.nearby(location = location)
            .onSuccess { emit(it) }
            .onFailure { emit(emptyList()) }
    }

    private fun things(playerID: String) = flow {
        thingsRepository.things(playerID)
            .onSuccess { emit(it) }
            .onFailure { emit(emptyList()) }
    }

    private fun players(location: Point) = flow {
        playerRepository.nearby(location = location)
            .onSuccess { emit(it) }
            .onFailure { emit(emptyList()) }
    }

    private val friends = flow {
        emit(emptyList<Player.Listing>())
    }
}
