package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.MatchClue
import com.micrantha.skouter.domain.model.MatchProof
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.platform.scan.CameraImage
import kotlinx.coroutines.flow.Flow

interface ClueRepository<T : Clue<*>, Proof : Collection<T>> {
    suspend fun analyze(image: CameraImage): Result<Proof>

    fun results(): Flow<T>
}

typealias LabelRepository = ClueRepository<LabelClue, LabelProof>
typealias DetectRepository = ClueRepository<DetectClue, DetectProof>
typealias ColorRepository = ClueRepository<ColorClue, ColorProof>
typealias SegmentRepository = ClueRepository<SegmentClue, SegmentProof>
typealias MatchRepository = ClueRepository<MatchClue, MatchProof>