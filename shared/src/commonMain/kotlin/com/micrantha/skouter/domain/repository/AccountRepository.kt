package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.models.Player

interface AccountRepository {
    suspend fun account(): Result<Player>

    val currentPlayer: Player?

    suspend fun isLoggedIn(): Boolean

    suspend fun loginAnonymous(): Result<Unit>

    suspend fun login(email: String, passwd: String): Result<Player>
}
