package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.source.MatchCaptureLocalSource
import com.micrantha.skouter.domain.repository.MatchRepository
import com.micrantha.skouter.platform.scan.CameraImage
import org.kodein.di.DI
import org.kodein.di.DIAware

class MatchDataRepository(
    override val di: DI,
    private val localSource: MatchCaptureLocalSource,
) : DIAware, MatchRepository {
    override suspend fun analyze(image: CameraImage) = localSource.analyze(image)
}
