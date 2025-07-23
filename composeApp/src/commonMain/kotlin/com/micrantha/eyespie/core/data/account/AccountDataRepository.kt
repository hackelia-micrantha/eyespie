package com.micrantha.eyespie.core.data.account

import com.micrantha.eyespie.core.data.account.mapping.AccountDomainMapper
import com.micrantha.eyespie.core.data.account.model.CurrentSession
import com.micrantha.eyespie.core.data.account.source.AccountRemoteSource
import com.micrantha.eyespie.domain.entities.Session
import com.micrantha.eyespie.domain.repository.AccountRepository

class AccountDataRepository(
    private val remoteSource: AccountRemoteSource,
    private val currentSession: CurrentSession,
    private val mapper: AccountDomainMapper,
) : AccountRepository {

    override suspend fun session() = remoteSource.account()
        .map(mapper::map).onSuccess {
        currentSession.update(it)
    }

    override suspend fun isLoggedIn() = remoteSource.isLoggedIn()

    override suspend fun login(email: String, passwd: String): Result<Session> {
        return remoteSource.login(email, passwd).mapCatching {
            session().getOrThrow()
        }
    }

    override suspend fun loginAnonymous() = remoteSource.loginAnonymous()

    override suspend fun loginWithGoogle() = remoteSource.loginWithGoogle().mapCatching {
        session().getOrThrow()
    }

    override suspend fun register(email: String, passwd: String) = remoteSource.register(email, passwd)

    override suspend fun registerWithGoogle() = remoteSource.registerWithGoogle()
}
