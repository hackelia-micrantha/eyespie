package com.micrantha.eyespie.features.players.domain.repository

import com.micrantha.eyespie.features.players.domain.entities.Player
import com.micrantha.eyespie.features.players.domain.entities.PlayerList

interface PlayerRepository {

    suspend fun players(): Result<PlayerList>

    suspend fun player(userId: String): Result<Player>

    suspend fun create(userId: String, firstName: String, lastName: String, nickName: String): Result<Player>
}