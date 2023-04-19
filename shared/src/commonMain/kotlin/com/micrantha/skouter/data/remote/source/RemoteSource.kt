package com.micrantha.skouter.data.remote.source

import com.micrantha.skouter.data.remote.ApiClient
import com.micrantha.skouter.data.remote.ApiMapper
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Player

class RemoteSource(
    private val client: ApiClient,
    private val mapper: ApiMapper = ApiMapper()
) {
    suspend fun loginAnonymous(): Result<String> = try {
        client.loginAnonymous().execute().let {
            val data = it.dataAssertNoErrors
            Result.success(data.accountCreateAnonymousSession!!.userId)
        }
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend fun account(): Result<Player> = try {
        client.account().execute().let {
            val data = it.dataAssertNoErrors.accountGet!!
            Result.success(mapper(data))
        }
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend fun games(): Result<List<Game>> = try {
        client.games().execute().let { resp ->
            val data = resp.dataAssertNoErrors.documents!!.games?.filterNotNull()
            Result.success(data?.map { mapper(it) } ?: emptyList())
        }
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
