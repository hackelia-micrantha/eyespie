package com.micrantha.eyespie.domain.repository

import com.micrantha.eyespie.domain.entities.Session

interface AccountRepository {
    suspend fun session(): Result<Session>

    suspend fun isLoggedIn(): Boolean

    suspend fun loginAnonymous(): Result<Unit>

    suspend fun login(email: String, passwd: String): Result<Session>

    suspend fun loginWithGoogle(): Result<Session>

    suspend fun register(email: String, passwd: String): Result<Unit>

    suspend fun registerWithGoogle(): Result<Unit>
}
