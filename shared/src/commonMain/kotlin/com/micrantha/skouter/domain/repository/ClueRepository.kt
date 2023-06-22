package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.platform.CameraImage

interface ClueRepository {

    suspend fun label(image: CameraImage): Result<LabelProof>

    suspend fun color(image: CameraImage): Result<ColorProof>

    suspend fun detect(image: CameraImage): Result<DetectProof>

    suspend fun segments(image: CameraImage): Result<SegmentProof>

    suspend fun match(image: CameraImage): Result<MatchProof>
}
