package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.Game
import com.micrantha.skouter.domain.model.GameList

interface GameRepository {
    suspend fun games(): Result<GameList>

    suspend fun game(id: String): Result<Game>

    //suspend fun nearby(): Result<GameList>
}
