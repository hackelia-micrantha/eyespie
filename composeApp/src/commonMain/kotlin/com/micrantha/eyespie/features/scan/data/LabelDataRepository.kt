package com.micrantha.eyespie.features.scan.data

import com.micrantha.eyespie.domain.entities.LabelProof
import com.micrantha.eyespie.domain.repository.LabelRepository
import com.micrantha.eyespie.features.scan.data.mapping.ClueDomainMapper
import com.micrantha.eyespie.features.scan.data.source.LabelCaptureLocalSource
import com.micrantha.eyespie.features.scan.data.source.LabelRemoteSource
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
