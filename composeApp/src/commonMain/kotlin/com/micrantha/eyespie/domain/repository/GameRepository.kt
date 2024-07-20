package com.micrantha.eyespie.domain.repository

import com.micrantha.eyespie.domain.model.Game
import com.micrantha.eyespie.domain.model.GameList

interface GameRepository {
    suspend fun games(): Result<GameList>

    suspend fun game(id: String): Result<Game>

    //suspend fun nearby(): Result<GameList>
}
