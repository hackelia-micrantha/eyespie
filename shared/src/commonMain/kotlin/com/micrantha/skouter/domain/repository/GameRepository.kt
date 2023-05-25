package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.GameList

interface GameRepository {
    suspend fun games(): Result<GameList>

    suspend fun game(id: String): Result<Game>

    //suspend fun nearby(): Result<GameList>
}
