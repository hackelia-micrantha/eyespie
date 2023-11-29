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

interface LabelRepository {

    suspend fun label(image: CameraImage): Result<LabelProof>

    fun labelAsync(image: CameraImage)

    fun labels(): Flow<LabelClue>
}

interface ColorRepository {
    suspend fun color(image: CameraImage): Result<ColorProof>

    fun colorAsync(image: CameraImage)

    fun colors(): Flow<ColorClue>
}

interface DetectionRepository {
    suspend fun detect(image: CameraImage): Result<DetectProof>

    fun detectAsync(image: CameraImage)

    fun detections(): Flow<DetectClue>
}

interface SegmentRepository {
    suspend fun segment(image: CameraImage): Result<SegmentProof>

    fun segmentAsync(image: CameraImage)

    fun segments(): Flow<SegmentClue>
}

interface MatchRepository {
    suspend fun match(image: CameraImage): Result<MatchProof>

    fun matchAsync(image: CameraImage)

    fun matches(): Flow<MatchClue>

}
