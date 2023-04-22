package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.models.GameListing

interface GameRepository {
    suspend fun games(): Result<List<GameListing>>
}
