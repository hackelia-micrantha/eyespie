package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.ObjectCaptureLocalSource
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.repository.DetectionRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.model.ScanObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware

class ObjectDataRepository(
    override val di: DI,
    private val captureSource: ObjectCaptureLocalSource,
    private val mapper: ClueDomainMapper
) : DetectionRepository, DIAware {

    private val store = RepositoryStore<ScanObjects>()

    override suspend fun capture(image: CameraImage): Result<DetectProof> {
        return captureSource.analyzeCapture(image).onSuccess(store::update).map(mapper::detect)
    }

    override fun stream(image: CameraImage) {
        return captureSource.analyzeStream(image, store::update)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun detections(): Flow<DetectClue> {
        return store.value.flatMapConcat {
            it.map(mapper::detect).asFlow()
        }
    }
}
