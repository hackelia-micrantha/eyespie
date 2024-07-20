package com.micrantha.eyespie.domain.repository

interface RealtimeRepository {

    suspend fun start()

    fun stop()

    suspend fun pause()
}
