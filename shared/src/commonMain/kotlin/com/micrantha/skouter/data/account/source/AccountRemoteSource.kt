package com.micrantha.skouter.data.account.source

import com.micrantha.skouter.data.account.model.AccountResponse
import com.micrantha.skouter.data.player.model.PlayerResponse
import com.micrantha.skouter.data.remote.SupaClient
import io.github.jan.supabase.gotrue.providers.builtin.Email

class AccountRemoteSource(
    private val client: SupaClient
) {
    fun isLoggedIn(): Boolean = client.auth().currentSessionOrNull()?.user != null

    fun session() = client.auth().currentSessionOrNull()

    suspend fun account() = try {
        val session = client.auth().currentSessionOrNull()!!
        val userId = session.user!!.id
        val player = client.players().select {
            eq("user_id", userId)
            limit(1)
        }.decodeAs<List<PlayerResponse>>().first()
        Result.success(AccountResponse(session.accessToken, session.refreshToken, player))
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
