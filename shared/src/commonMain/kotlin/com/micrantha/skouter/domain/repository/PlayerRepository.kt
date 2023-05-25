package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.models.ThingList

interface PlayerRepository {
    suspend fun things(playerID: String): Result<ThingList>
}
