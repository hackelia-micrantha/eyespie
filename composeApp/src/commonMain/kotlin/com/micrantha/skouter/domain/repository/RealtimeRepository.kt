package com.micrantha.skouter.domain.repository

interface RealtimeRepository {

    suspend fun start()

    fun stop()

    suspend fun pause()
}
