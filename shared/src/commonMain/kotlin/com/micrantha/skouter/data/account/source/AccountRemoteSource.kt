package com.micrantha.skouter.data.account.source

import com.micrantha.skouter.data.player.mapping.PlayerDomainMapper
import com.micrantha.skouter.data.player.model.PlayerResponse
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.domain.models.Player
import io.github.jan.supabase.gotrue.providers.builtin.Email

class AccountRemoteSource(
    private val client: SupaClient,
    private val mapper: PlayerDomainMapper,
) {
    fun isLoggedIn(): Boolean = client.auth().currentSessionOrNull()?.user != null

    suspend fun account(): Result<Player> = try {
        val userId = client.auth().currentSessionOrNull()!!.user!!.id
        val player = client.players().select {
            eq("user_id", userId)
            limit(1)
        }.decodeAs<List<PlayerResponse>>().first()
        Result.success<Player>(mapper.map(player))
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
