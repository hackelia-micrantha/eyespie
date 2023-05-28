package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.source.LabelLocalSource
import com.micrantha.skouter.data.clue.source.LabelRemoteSource
import com.micrantha.skouter.domain.model.CameraImage
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.repository.ClueRepository

class ClueDataRepository(
    private val labelLocalSource: LabelLocalSource,
    private val labelRemoteSource: LabelRemoteSource,
    private val mapper: ClueDomainMapper,
) : ClueRepository {
    override suspend fun recognize(image: ByteArray, contentType: String) =
        labelRemoteSource.recognize(image, contentType).map(mapper::recognition)

    override suspend fun label(image: CameraImage): Result<LabelProof> {
        return labelLocalSource.analyze(image).map(mapper::labels)
    }
}
