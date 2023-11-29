package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.MatchCaptureLocalSource
import com.micrantha.skouter.data.clue.source.MatchStreamLocalSource
import com.micrantha.skouter.domain.repository.MatchRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.kodein.di.on

class MatchDataRepository(
    override val di: DI,
    private val captureSource: MatchCaptureLocalSource,
    private val mapper: ClueDomainMapper
) : DIAware, MatchRepository {

    private val streamSource by on(this).instance<AnalyzerCallback<ImageEmbeddings>, MatchStreamLocalSource>(
        arg = AnalyzerCallback { store.update(it) }
    )

    private val store = RepositoryStore<ImageEmbeddings>()

    override suspend fun match(image: CameraImage) =
        captureSource.analyze(image).onSuccess(store::update).map(mapper::match)

    override fun matchAsync(image: CameraImage) = streamSource.analyze(image)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun matches() = store.value.flatMapConcat {
        it.map(mapper::match).asFlow()
    }
}
