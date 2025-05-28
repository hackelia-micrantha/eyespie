package com.micrantha.eyespie.domain.repository

import com.micrantha.eyespie.domain.entities.Game
import com.micrantha.eyespie.domain.entities.GameList

interface GameRepository {
    suspend fun games(): Result<GameList>

    suspend fun game(id: String): Result<Game>

    //suspend fun nearby(): Result<GameList>
}
