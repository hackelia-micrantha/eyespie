package com.micrantha.eyespie.domain.repository

import com.micrantha.eyespie.domain.model.Session

interface AccountRepository {
    suspend fun session(): Result<Session>

    suspend fun isLoggedIn(): Boolean

    suspend fun loginAnonymous(): Result<Unit>

    suspend fun login(email: String, passwd: String): Result<Session>
}
