package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.source.ColorLocalSource
import com.micrantha.skouter.data.clue.source.ImageLocalSource
import com.micrantha.skouter.data.clue.source.LabelRemoteSource
import com.micrantha.skouter.data.clue.source.ObjectLocalSource
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.repository.ClueRepository
import com.micrantha.skouter.platform.CameraImage

class ClueDataRepository(
    private val labelLocalSource: ImageLocalSource,
    private val labelRemoteSource: LabelRemoteSource,
    private val colorLocalSource: ColorLocalSource,
    private val objectLocalSource: ObjectLocalSource,
    private val mapper: ClueDomainMapper,
) : ClueRepository {

    @Deprecated("do not want to have dependency on backend for functionality")
    override suspend fun recognize(image: ByteArray, contentType: String): Result<LabelProof> =
        labelRemoteSource.recognize(image, contentType).map(mapper::recognition)

    override suspend fun label(image: CameraImage): Result<LabelProof> {
        return labelLocalSource.analyze(image).map(mapper::label)
    }

    override suspend fun color(image: CameraImage): Result<ColorProof> {
        return colorLocalSource.analyze(image).map(mapper::color)
    }

    override suspend fun recognize(image: CameraImage): Result<DetectProof> {
        // TODO: there is more to do here with tracking identified
        // objects and running analyzers on sub images
        // could eventually become the main entry point
        // but for now will collect from the entire image
        return objectLocalSource.analyze(image).map(mapper::detect)
    }
}
