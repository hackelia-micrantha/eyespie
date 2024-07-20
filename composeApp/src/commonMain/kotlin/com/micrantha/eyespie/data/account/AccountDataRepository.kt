package com.micrantha.eyespie.data.account

import com.micrantha.eyespie.data.account.mapping.AccountDomainMapper
import com.micrantha.eyespie.data.account.model.CurrentSession
import com.micrantha.eyespie.data.account.source.AccountRemoteSource
import com.micrantha.eyespie.domain.model.Session
import com.micrantha.eyespie.domain.repository.AccountRepository

class AccountDataRepository(
    private val remoteSource: AccountRemoteSource,
    private val currentSession: CurrentSession,
    private val mapper: AccountDomainMapper,
) : AccountRepository {

    override suspend fun session() = remoteSource.account().map { mapper.map(it) }.onSuccess {
        currentSession.update(it)
    }

    override suspend fun isLoggedIn() = remoteSource.isLoggedIn()

    override suspend fun login(email: String, passwd: String): Result<Session> {
        return remoteSource.login(email, passwd).mapCatching {
            session().getOrThrow()
        }
    }

    override suspend fun loginAnonymous() = remoteSource.loginAnonymous()
}