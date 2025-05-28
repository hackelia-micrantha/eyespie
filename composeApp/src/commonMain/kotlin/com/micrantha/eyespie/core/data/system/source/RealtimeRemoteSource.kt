package com.micrantha.eyespie.core.data.system.source

import com.micrantha.eyespie.core.data.client.SupaRealtimeClient

class RealtimeRemoteSource(
    private val supaRealtimeClient: SupaRealtimeClient
) {
    suspend fun connect() = supaRealtimeClient.connect()

    suspend fun block() = supaRealtimeClient.block()

    fun disconnect() = supaRealtimeClient.disconnect()

    fun subscribe(table: String) = supaRealtimeClient.subscribe(table)
}
