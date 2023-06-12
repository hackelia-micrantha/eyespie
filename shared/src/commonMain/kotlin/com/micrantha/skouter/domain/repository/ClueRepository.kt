package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.model.DetectProof
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.SegmentProof
import com.micrantha.skouter.platform.CameraImage

interface ClueRepository {

    @Deprecated("use local recognition instead")
    suspend fun recognize(image: ByteArray, contentType: String): Result<LabelProof>

    suspend fun label(image: CameraImage): Result<LabelProof>

    suspend fun color(image: CameraImage): Result<ColorProof>

    suspend fun recognize(image: CameraImage): Result<DetectProof>

    suspend fun segments(image: CameraImage): Result<SegmentProof>
}
