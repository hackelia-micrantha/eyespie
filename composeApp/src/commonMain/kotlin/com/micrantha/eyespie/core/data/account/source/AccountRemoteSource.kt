package com.micrantha.eyespie.core.data.account.source

import com.micrantha.eyespie.core.data.account.model.AccountResponse
import com.micrantha.eyespie.core.data.client.SupaClient
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email

class AccountRemoteSource(
    private val client: SupaClient
) {
    fun isLoggedIn(): Boolean = client.auth().currentSessionOrNull()?.user != null

    suspend fun account() = try {
        val session = client.auth().apply { loadFromStorage() }.currentSessionOrNull()!!
        val user = session.user!!
        Result.success(AccountResponse(session.accessToken, session.refreshToken, user.id))
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend fun loginAnonymous() = try {
        client.auth().signInAnonymously()
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend fun login(email: String, password: String) = try {
        client.auth().signInWith(Email) {
            this.email = email
            this.password = password
        }
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend fun loginWithGoogle() = try {
        client.auth().signInWith(Google)
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend fun register(email: String, password: String) = try {
        client.auth().signUpWith(Email) {
            this.email = email
            this.password = password
        }
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend fun registerWithGoogle() = try {
        client.auth().signUpWith(Google)
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
