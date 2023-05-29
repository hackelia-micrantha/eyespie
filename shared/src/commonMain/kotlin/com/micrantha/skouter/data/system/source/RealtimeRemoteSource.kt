package com.micrantha.skouter.data.system.source

import com.micrantha.skouter.data.client.SupaRealtimeClient

class RealtimeRemoteSource(
    private val supaRealtimeClient: SupaRealtimeClient
) {
    suspend fun connect() = supaRealtimeClient.connect()

    suspend fun block() = supaRealtimeClient.block()

    fun disconnect() = supaRealtimeClient.disconnect()

    fun things() = supaRealtimeClient.subscribe("Thing")
}
