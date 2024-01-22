package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.source.SegmentCaptureLocalSource
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.domain.repository.SegmentRepository
import com.micrantha.skouter.platform.scan.CameraImage
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
