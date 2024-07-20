package com.micrantha.eyespie.data.system.source

import com.micrantha.eyespie.data.client.SupaRealtimeClient

class RealtimeRemoteSource(
    private val supaRealtimeClient: SupaRealtimeClient
) {
    suspend fun connect() = supaRealtimeClient.connect()

    suspend fun block() = supaRealtimeClient.block()

    fun disconnect() = supaRealtimeClient.disconnect()

    fun subscribe(table: String) = supaRealtimeClient.subscribe(table)
}
