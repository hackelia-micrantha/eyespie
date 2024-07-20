package com.micrantha.eyespie.data.clue

import com.micrantha.eyespie.data.clue.source.SegmentCaptureLocalSource
import com.micrantha.eyespie.domain.model.SegmentProof
import com.micrantha.eyespie.domain.repository.SegmentRepository
import com.micrantha.eyespie.platform.scan.CameraImage
import org.kodein.di.DI
import org.kodein.di.DIAware

class SegmentDataRepository(
    override val di: DI,
    private val localSource: SegmentCaptureLocalSource,
) : DIAware, SegmentRepository {
    override suspend fun analyze(image: CameraImage): Result<SegmentProof> {
        return localSource.analyze(image)
    }
}
