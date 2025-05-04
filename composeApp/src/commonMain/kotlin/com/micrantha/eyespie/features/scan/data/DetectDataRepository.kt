package com.micrantha.eyespie.features.scan.data

import com.micrantha.eyespie.features.scan.data.source.DetectCaptureLocalSource
import com.micrantha.eyespie.domain.entities.DetectProof
import com.micrantha.eyespie.domain.repository.DetectRepository
import com.micrantha.eyespie.platform.scan.CameraImage
import org.kodein.di.DI
import org.kodein.di.DIAware

class DetectDataRepository(
    override val di: DI,
    private val localSource: DetectCaptureLocalSource,
) : DetectRepository, DIAware {
    override suspend fun analyze(image: CameraImage): Result<DetectProof> {
        return localSource.analyze(image)
    }
}
