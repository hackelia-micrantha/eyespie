package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.domain.model.LabelProof

interface ClueRepository {

    suspend fun recognize(image: ByteArray, contentType: String): Result<LabelProof>

    suspend fun label(image: CameraImage): Result<LabelProof>
}
