package com.micrantha.eyespie.domain.repository

import com.micrantha.eyespie.domain.model.PlayerList

interface PlayerRepository {

    suspend fun players(): Result<PlayerList>
}
