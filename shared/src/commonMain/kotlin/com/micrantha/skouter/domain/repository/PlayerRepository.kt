package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.models.PlayerList

interface PlayerRepository {

    suspend fun players(): Result<PlayerList>
}
