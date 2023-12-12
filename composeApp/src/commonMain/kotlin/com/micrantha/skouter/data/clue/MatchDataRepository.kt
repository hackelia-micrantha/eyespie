package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.MatchCaptureLocalSource
import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.domain.repository.MatchRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.model.ScanEmbedding
import kotlinx.coroutines.flow.map
import org.kodein.di.DI
import org.kodein.di.DIAware

class MatchDataRepository(
    override val di: DI,
    private val captureSource: MatchCaptureLocalSource,
    private val mapper: ClueDomainMapper
) : DIAware, MatchRepository {
    private val store = RepositoryStore<ScanEmbedding>()

    override suspend fun capture(image: CameraImage): Result<MatchProof> =
        captureSource.analyzeCapture(image).onSuccess(store::update).map(mapper::match)

    override fun stream(image: CameraImage) = captureSource.analyzeStream(image, store::update)

    override fun matches() = store.value.map(mapper::match)
}
