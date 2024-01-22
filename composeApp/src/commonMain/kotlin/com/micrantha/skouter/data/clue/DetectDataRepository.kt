package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.source.DetectCaptureLocalSource
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.repository.DetectRepository
import com.micrantha.skouter.platform.scan.CameraImage
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
