package com.micrantha.skouter.domain.repository

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

interface CameraRepository<out T> {
    suspend fun analyze(image: CameraImage): Result<T>
}

interface LabelRepository : CameraRepository<LabelProof> {
    fun labels(): Flow<LabelClue>
}

interface ColorRepository : CameraRepository<ColorProof> {

    fun colors(): Flow<ColorClue>
}

interface DetectionRepository : CameraRepository<DetectProof> {

    fun detections(): Flow<DetectClue>
}

interface SegmentRepository : CameraRepository<SegmentProof> {

    fun segments(): Flow<SegmentClue>
}

interface MatchRepository : CameraRepository<MatchProof> {

    fun matches(): Flow<MatchClue>

}
