package com.micrantha.skouter.data.account

import com.micrantha.skouter.data.account.mapping.AccountDomainMapper
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.data.account.source.AccountRemoteSource
import com.micrantha.skouter.domain.model.Session
import com.micrantha.skouter.domain.repository.AccountRepository as DomainRepository

class AccountDataRepository(
    private val remoteSource: AccountRemoteSource,
    private val currentSession: CurrentSession,
    private val mapper: AccountDomainMapper,
) : DomainRepository {

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
