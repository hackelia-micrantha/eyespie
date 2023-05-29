package com.micrantha.skouter.data.system

import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.skouter.data.system.mapping.RealtimeDomainMapper
import com.micrantha.skouter.data.system.source.RealtimeRemoteSource
import com.micrantha.skouter.domain.repository.RealtimeRepository
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

    suspend fun things() = remoteSource.things().map(mapper::thing).onEach {
        dispatcher.dispatch(it)
    }.collect()
}
