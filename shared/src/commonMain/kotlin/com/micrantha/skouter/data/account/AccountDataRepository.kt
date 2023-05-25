package com.micrantha.skouter.data.account

import com.micrantha.skouter.data.account.model.CurrentAccount
import com.micrantha.skouter.data.account.source.AccountRemoteSource
import com.micrantha.skouter.domain.repository.AccountRepository as DomainRepository

class AccountDataRepository(
    private val remoteSource: AccountRemoteSource,
    private val currentAccount: CurrentAccount,
) : DomainRepository {

    override val currentPlayer = currentAccount.player()
    
    override suspend fun account() = remoteSource.account().onSuccess {
        currentAccount.update(it)
    }

    override suspend fun isLoggedIn() = remoteSource.isLoggedIn()
    override suspend fun login(email: String, passwd: String) = remoteSource.login(email, passwd)
    override suspend fun loginAnonymous() = remoteSource.loginAnonymous()
}
