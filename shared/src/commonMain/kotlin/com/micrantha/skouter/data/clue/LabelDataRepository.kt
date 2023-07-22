package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.LabelCaptureLocalSource
import com.micrantha.skouter.data.clue.source.LabelStreamLocalSource
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.repository.LabelRepository
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.model.ImageLabels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.kodein.di.on

class LabelDataRepository(
    override val di: DI,
    private val captureSource: LabelCaptureLocalSource,
    private val mapper: ClueDomainMapper
) : LabelRepository, DIAware {

    private val streamSource by on(this).instance<AnalyzerCallback<ImageLabels>, LabelStreamLocalSource>(
        arg = AnalyzerCallback {
            store.update(it)
        }
    )

    private val store = RepositoryStore<ImageLabels>()

    override suspend fun label(image: CameraImage): Result<LabelProof> {
        return captureSource.analyze(image)
            .onSuccess(store::update)
            .map(mapper::labels)
    }

    override fun labelAsync(image: CameraImage) {
        streamSource.analyze(image)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun labels() = store.value.flatMapConcat {
        it.map(mapper::label).asFlow()
    }

}
