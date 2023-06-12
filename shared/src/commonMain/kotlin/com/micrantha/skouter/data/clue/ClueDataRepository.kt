package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.source.ColorLocalSource
import com.micrantha.skouter.data.clue.source.ImageLocalSource
import com.micrantha.skouter.data.clue.source.LabelRemoteSource
import com.micrantha.skouter.data.clue.source.ObjectLocalSource
import com.micrantha.skouter.data.clue.source.SegmentLocalSource
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.domain.repository.ClueRepository
import com.micrantha.skouter.platform.CameraImage

class ClueDataRepository(
    private val labelLocalSource: ImageLocalSource,
    private val labelRemoteSource: LabelRemoteSource,
    private val colorLocalSource: ColorLocalSource,
    private val objectLocalSource: ObjectLocalSource,
    private val segmentLocalSource: SegmentLocalSource,
    private val mapper: ClueDomainMapper,
) : ClueRepository {

    @Deprecated("use local label source")
    override suspend fun recognize(image: ByteArray, contentType: String): Result<LabelProof> =
        labelRemoteSource.recognize(image, contentType).map(mapper::recognition)

    override suspend fun label(image: CameraImage): Result<LabelProof> {
        return labelLocalSource.analyze(image).map(mapper::label)
    }

    override suspend fun color(image: CameraImage): Result<ColorProof> {
        return colorLocalSource.analyze(image).map(mapper::color)
    }

    override suspend fun recognize(image: CameraImage): Result<DetectProof> {
        return objectLocalSource.analyze(image).map(mapper::detect)
    }

    override suspend fun segments(image: CameraImage): Result<SegmentProof> {
        return segmentLocalSource.analyze(image).map(mapper::segment)
    }
}
