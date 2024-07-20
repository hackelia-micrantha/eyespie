package com.micrantha.eyespie.data.clue

import com.micrantha.eyespie.data.clue.mapping.ClueDomainMapper
import com.micrantha.eyespie.data.clue.source.LabelCaptureLocalSource
import com.micrantha.eyespie.data.clue.source.LabelRemoteSource
import com.micrantha.eyespie.domain.model.LabelProof
import com.micrantha.eyespie.domain.repository.LabelRepository
import com.micrantha.eyespie.platform.scan.CameraImage
import org.kodein.di.DI
import org.kodein.di.DIAware

class LabelDataRepository(
    override val di: DI,
    private val localSource: LabelCaptureLocalSource,
    private val remoteSource: LabelRemoteSource,
    private val mapper: ClueDomainMapper
) : LabelRepository, DIAware {
    override suspend fun analyze(image: CameraImage): Result<LabelProof> {
        // TODO: switch sources from user preference and connectivity
        return localSource.analyze(image)
    }

    suspend fun infer(image: CameraImage): Result<LabelProof> {
        return remoteSource.analyze(image)
            .map(mapper::response)
    }
}
