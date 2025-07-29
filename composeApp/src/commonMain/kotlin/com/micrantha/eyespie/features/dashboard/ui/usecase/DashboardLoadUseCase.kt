package com.micrantha.eyespie.features.dashboard.ui.usecase

import com.micrantha.bluebell.domain.usecase.flowUseCase
import com.micrantha.eyespie.core.data.account.model.CurrentSession
import com.micrantha.eyespie.domain.entities.Location.Point
import com.micrantha.eyespie.domain.repository.ThingRepository
import com.micrantha.eyespie.features.dashboard.ui.DashboardAction.Loaded
import com.micrantha.eyespie.features.players.domain.repository.PlayerRepository
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
            flow = things(location, player.id),
            flow2 = friends,
            flow3 = players
        ) { things, friends, players ->
            Loaded(things, friends, players)
        }
    }

    private fun things(location: Point?, playerID: String) = flow {
        val res = if (location != null) {
            thingsRepository.nearby(location = location)
        } else {
            thingsRepository.things(playerID)
        }
        res.onSuccess { emit(it) }
    }

    private val friends = flow {
        playerRepository.players()
            .onSuccess { emit(it) }
    }


    private val players = flow {
        playerRepository.players()
            .onSuccess { emit(it) }
    }
}
