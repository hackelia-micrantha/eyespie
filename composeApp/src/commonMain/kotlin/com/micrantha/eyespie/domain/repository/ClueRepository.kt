package com.micrantha.eyespie.domain.repository

import com.micrantha.eyespie.domain.model.Clue
import com.micrantha.eyespie.domain.model.ColorClue
import com.micrantha.eyespie.domain.model.ColorProof
import com.micrantha.eyespie.domain.model.DetectClue
import com.micrantha.eyespie.domain.model.DetectProof
import com.micrantha.eyespie.domain.model.LabelClue
import com.micrantha.eyespie.domain.model.LabelProof
import com.micrantha.eyespie.domain.model.MatchClue
import com.micrantha.eyespie.domain.model.MatchProof
import com.micrantha.eyespie.domain.model.SegmentClue
import com.micrantha.eyespie.domain.model.SegmentProof
import com.micrantha.eyespie.platform.scan.CameraImage

interface ClueRepository<T : Clue<*>, Proof : Collection<T>> {
    suspend fun analyze(image: CameraImage): Result<Proof>
}

typealias LabelRepository = ClueRepository<LabelClue, LabelProof>
typealias DetectRepository = ClueRepository<DetectClue, DetectProof>
typealias ColorRepository = ClueRepository<ColorClue, ColorProof>
typealias SegmentRepository = ClueRepository<SegmentClue, SegmentProof>
typealias MatchRepository = ClueRepository<MatchClue, MatchProof>
