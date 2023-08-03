package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.ObjectCaptureLocalSource
import com.micrantha.skouter.data.clue.source.ObjectStreamLocalSource
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.repository.DetectionRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.AnalyzerCallback
import com.micrantha.skouter.platform.scan.model.ImageObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.kodein.di.on

class ObjectDataRepository(
    override val di: DI,
    private val captureSource: ObjectCaptureLocalSource,
    private val mapper: ClueDomainMapper
) : DetectionRepository, DIAware {

    private val store = RepositoryStore<ImageObjects>()

    private val streamSource by on(this).instance<AnalyzerCallback<ImageObjects>, ObjectStreamLocalSource>(
        arg = AnalyzerCallback {
            store.update(it)
        }
    )

    override suspend fun detect(image: CameraImage): Result<DetectProof> {
        return captureSource.analyze(image).onSuccess(store::update).map(mapper::detect)
    }

    override fun detectAsync(image: CameraImage) {
        return streamSource.analyze(image)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun detections(): Flow<DetectClue> {
        return store.value.flatMapConcat {
            it.map(mapper::detect).asFlow()
        }
    }
}
