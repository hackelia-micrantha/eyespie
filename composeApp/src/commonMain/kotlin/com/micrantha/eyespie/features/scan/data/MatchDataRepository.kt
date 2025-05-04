package com.micrantha.eyespie.features.scan.data

import com.micrantha.eyespie.features.scan.data.source.MatchCaptureLocalSource
import com.micrantha.eyespie.domain.repository.MatchRepository
import com.micrantha.eyespie.platform.scan.CameraImage
import org.kodein.di.DI
import org.kodein.di.DIAware

class MatchDataRepository(
    override val di: DI,
    private val localSource: MatchCaptureLocalSource,
) : DIAware, MatchRepository {
    override suspend fun analyze(image: CameraImage) = localSource.analyze(image)
}
