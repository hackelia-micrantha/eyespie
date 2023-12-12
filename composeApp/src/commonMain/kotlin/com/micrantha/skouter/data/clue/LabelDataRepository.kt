package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.LabelCaptureLocalSource
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.repository.LabelRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.model.ScanLabels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware

class LabelDataRepository(
    override val di: DI,
    private val captureSource: LabelCaptureLocalSource,
    private val mapper: ClueDomainMapper
) : LabelRepository, DIAware {
    private val store = RepositoryStore<ScanLabels>()

    override suspend fun capture(image: CameraImage): Result<LabelProof> {
        return captureSource.analyzeCapture(image)
            .onSuccess(store::update)
            .map(mapper::labels)
    }

    override fun stream(image: CameraImage) {
        captureSource.analyzeStream(image, store::update)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun labels() = store.value.flatMapConcat {
        it.map(mapper::label).asFlow()
    }

}
