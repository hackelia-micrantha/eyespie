package com.micrantha.skouter.data.clue

import com.micrantha.bluebell.data.SimpleStore
import com.micrantha.skouter.data.clue.source.SegmentCaptureLocalSource
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.domain.repository.SegmentRepository
import com.micrantha.skouter.platform.scan.CameraImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware

class SegmentDataRepository(
    override val di: DI,
    private val captureSource: SegmentCaptureLocalSource,
) : DIAware, SegmentRepository {

    private val store = SimpleStore<SegmentProof>()

    override suspend fun analyze(image: CameraImage): Result<SegmentProof> {
        return captureSource.analyze(image).onSuccess(store::update)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun results() = store.value.flatMapConcat { it.asFlow() }
}
