package com.micrantha.skouter.data.client

import com.benasher44.uuid.uuid4
import com.micrantha.skouter.SkouterConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class SupaRealtimeClient {
    private val realtime by lazy {
        createSupabaseClient(SkouterConfig.supaBaseUrl, SkouterConfig.supaBaseKey) {
            install(RealtimeClient)
        }.realtime
    }

    suspend fun connect() = realtime.connect()

    fun disconnect() = realtime.disconnect()

    suspend fun block() = realtime.block()

    fun subscribe(table: String, key: String = uuid4().toString()): Flow<PostgresAction> {
        val channel = realtime.subscriptions[key] ?: realtime.channel(key)
        return channel.postgresChangeFlow<PostgresAction>("public") {
            this.table = table
        }.onCompletion {
            channel.unsubscribe()
        }.onStart {
            channel.subscribe()
        }
    }
}
