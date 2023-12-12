package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.SegmentCaptureLocalSource
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.domain.repository.SegmentRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.model.ScanSegments
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware

class SegmentDataRepository(
    override val di: DI,
    private val captureSource: SegmentCaptureLocalSource,
    private val mapper: ClueDomainMapper
) : DIAware, SegmentRepository {

    private val store = RepositoryStore<ScanSegments>()

    override suspend fun capture(image: CameraImage): Result<SegmentProof> {
        return captureSource.analyzeCapture(image).onSuccess(store::update).map(mapper::segment)
    }

    override fun stream(image: CameraImage) {
        captureSource.analyzeStream(image, store::update)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun segments(): Flow<SegmentClue> {
        return store.value.flatMapConcat {
            it.map(mapper::segment).asFlow()
        }
    }
}
