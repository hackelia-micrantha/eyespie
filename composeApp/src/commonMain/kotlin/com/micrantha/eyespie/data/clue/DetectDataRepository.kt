package com.micrantha.eyespie.data.clue

import com.micrantha.eyespie.data.clue.source.DetectCaptureLocalSource
import com.micrantha.eyespie.domain.model.DetectProof
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
