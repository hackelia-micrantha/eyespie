package com.micrantha.skouter.data.accounts.source

import com.micrantha.skouter.data.remote.ApiClient
import com.micrantha.skouter.domain.models.Player
import io.github.jan.supabase.gotrue.providers.builtin.Email

class AccountRemoteSource(
    private val client: ApiClient
) {
    fun isLoggedIn(): Boolean = client.auth().currentSessionOrNull()?.user != null

    suspend fun account(): Result<Player> = try {
        val userId = client.auth().currentSessionOrNull()!!.user!!.id
        val player = client.players().select {
            eq("user_id", userId)
            limit(1)
        }.decodeAs<Player>()
        Result.success(player)
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
