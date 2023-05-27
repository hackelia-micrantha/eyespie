package com.micrantha.skouter.data.account

import com.micrantha.skouter.data.account.model.CurrentAccount
import com.micrantha.skouter.data.account.source.AccountRemoteSource
import com.micrantha.skouter.data.player.mapping.PlayerDomainMapper
import com.micrantha.skouter.domain.model.Player
import com.micrantha.skouter.domain.repository.AccountRepository as DomainRepository

class AccountDataRepository(
    private val remoteSource: AccountRemoteSource,
    private val currentAccount: CurrentAccount,
    private val mapper: PlayerDomainMapper,
) : DomainRepository {

    override val currentPlayer = currentAccount.player()

    override suspend fun account() = remoteSource.account().map(mapper::map).onSuccess {
        currentAccount.update(it)
    }

    override suspend fun isLoggedIn() = remoteSource.isLoggedIn()
    
    override suspend fun login(email: String, passwd: String): Result<Player> {
        return remoteSource.login(email, passwd).mapCatching {
            account().getOrThrow()
        }
    }

    override suspend fun loginAnonymous() = remoteSource.loginAnonymous()
}
