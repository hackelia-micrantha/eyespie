package com.micrantha.skouter.data.clue

import com.micrantha.bluebell.data.MemoryStore
import com.micrantha.skouter.data.clue.source.MatchCaptureLocalSource
import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.domain.repository.MatchRepository
import com.micrantha.skouter.platform.scan.CameraImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware

class MatchDataRepository(
    override val di: DI,
    private val localSource: MatchCaptureLocalSource,
) : DIAware, MatchRepository {

    private val store = MemoryStore<MatchProof>()

    override suspend fun analyze(image: CameraImage) =
        localSource.analyze(image).onSuccess(store::update)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun results() = store.value.flatMapConcat { it.asFlow() }
}
