package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.data.clue.model.LabelResponse
import com.micrantha.skouter.domain.model.CameraImage

expect class LabelLocalSource {

    suspend fun analyze(image: CameraImage): Result<List<LabelResponse>>
}
