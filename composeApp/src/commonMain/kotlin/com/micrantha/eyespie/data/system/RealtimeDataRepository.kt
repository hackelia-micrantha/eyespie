package com.micrantha.eyespie.data.system

import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.eyespie.data.system.mapping.RealtimeDomainMapper
import com.micrantha.eyespie.data.system.source.RealtimeRemoteSource
import com.micrantha.eyespie.domain.repository.RealtimeRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class RealtimeDataRepository(
    private val dispatcher: Dispatcher,
    private val remoteSource: RealtimeRemoteSource,
    private val mapper: RealtimeDomainMapper
) : RealtimeRepository {

    override suspend fun start() = remoteSource.connect()

    override fun stop() = remoteSource.disconnect()

    override suspend fun pause() = remoteSource.block()

    suspend fun things() = remoteSource.subscribe("Thing").map(mapper::thing).onEach {
        dispatcher.dispatch(it)
    }.collect()
}
