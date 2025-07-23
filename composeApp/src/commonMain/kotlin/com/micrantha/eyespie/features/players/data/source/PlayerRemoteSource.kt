package com.micrantha.eyespie.features.players.data.source

import com.micrantha.eyespie.core.data.client.SupaClient
import com.micrantha.eyespie.features.players.data.model.PlayerResponse

class PlayerRemoteSource(
    private val supaClient: SupaClient
) {
    suspend fun players() = try {
        val result = supaClient.players().select()
            .decodeList<PlayerResponse>()
        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }

    suspend fun player(id: String) = try {
        val player = supaClient.players().select {
            filter {
                eq("user_id", id)
            }
            limit(1)
        }.decodeAs<List<PlayerResponse>>().first()
        Result.success(player)
    } catch (err: Throwable) {
        Result.failure(err)
    }

    suspend fun create(userId: String, firstName: String, lastName: String, nickName: String) = try {
        supaClient.players().insert(mapOf(
            "user_id" to userId,
            "first_name" to firstName,
            "last_name" to lastName,
            "nick_name" to nickName
        ))
        Result.success(Unit)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
