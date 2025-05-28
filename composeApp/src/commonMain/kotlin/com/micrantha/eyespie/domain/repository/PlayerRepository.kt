package com.micrantha.eyespie.domain.repository

import com.micrantha.eyespie.domain.entities.PlayerList

interface PlayerRepository {

    suspend fun players(): Result<PlayerList>
}
