package com.micrantha.eyespie.features.scan.data

import com.micrantha.eyespie.features.scan.data.source.SegmentCaptureLocalSource
import com.micrantha.eyespie.domain.entities.SegmentProof
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
