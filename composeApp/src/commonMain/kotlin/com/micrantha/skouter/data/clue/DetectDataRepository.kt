package com.micrantha.skouter.data.clue

import com.micrantha.bluebell.data.MemoryStore
import com.micrantha.skouter.data.clue.source.DetectCaptureLocalSource
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.repository.DetectRepository
import com.micrantha.skouter.platform.scan.CameraImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware

class DetectDataRepository(
    override val di: DI,
    private val localSource: DetectCaptureLocalSource,
) : DetectRepository, DIAware {

    private val store = MemoryStore<DetectProof>()

    override suspend fun analyze(image: CameraImage): Result<DetectProof> {
        return localSource.analyze(image).onSuccess(store::update)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun results() = store.value.flatMapConcat { it.asFlow() }
}
