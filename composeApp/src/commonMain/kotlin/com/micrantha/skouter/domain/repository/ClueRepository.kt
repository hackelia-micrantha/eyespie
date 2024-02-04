package com.micrantha.skouter.domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.Clues
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

interface ClueRepository<T : Clue<*>, Proof : Collection<T>> {
    suspend fun analyze(image: CameraImage): Result<Proof>
}

typealias LabelRepository = ClueRepository<LabelClue, LabelProof>
typealias DetectRepository = ClueRepository<DetectClue, DetectProof>
typealias ColorRepository = ClueRepository<ColorClue, ColorProof>
typealias SegmentRepository = ClueRepository<SegmentClue, SegmentProof>
typealias MatchRepository = ClueRepository<MatchClue, MatchProof>

interface GenerateRepository {
    suspend fun generate(from: Clues): Result<ImageBitmap>
}
