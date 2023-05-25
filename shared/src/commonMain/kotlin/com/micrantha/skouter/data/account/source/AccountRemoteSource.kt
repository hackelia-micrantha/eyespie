package com.micrantha.skouter.data.account.source

import com.micrantha.skouter.data.account.mapping.AccountDomainMapper
import com.micrantha.skouter.data.account.model.AccountResponse
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.domain.models.Player
import io.github.jan.supabase.gotrue.providers.builtin.Email

class AccountRemoteSource(
    private val client: SupaClient,
    private val mapper: AccountDomainMapper,
) {
    fun isLoggedIn(): Boolean = client.auth().currentSessionOrNull()?.user != null

    suspend fun account(): Result<Player> = try {
        val userId = client.auth().currentSessionOrNull()!!.user!!.id
        val player = client.players().select {
            eq("user_id", userId)
            limit(1)
        }.decodeAs<List<AccountResponse>>().first()
        Result.success(mapper(player))
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend fun loginAnonymous() = try {
        client.auth().signUpWith(Email) {
            email = "skouter-anon@micrantha.com"
            password = ""
        }
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend fun login(email: String, password: String) = try {
        client.auth().loginWith(Email) {
            this.email = email
            this.password = password
        }
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
