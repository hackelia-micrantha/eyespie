package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.LabelCaptureLocalSource
import com.micrantha.skouter.data.clue.source.LabelRemoteSource
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.repository.LabelRepository
import com.micrantha.skouter.platform.scan.CameraImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware

class LabelDataRepository(
    override val di: DI,
    private val captureSource: LabelCaptureLocalSource,
    private val remoteSource: LabelRemoteSource,
    private val mapper: ClueDomainMapper
) : LabelRepository, DIAware {
    private val store = RepositoryStore<LabelProof>()

    override suspend fun analyze(image: CameraImage): Result<LabelProof> {
        return captureSource.analyze(image)
            .map(mapper::labels)
            .onSuccess(store::update)
    }

    suspend fun analyzeRemote(image: CameraImage): Result<LabelProof> {
        return remoteSource.analyze(image)
            .map(mapper::response)
            .onSuccess(store::update)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun labels() = store.value.flatMapConcat {
        it.asFlow()
    }
}
