package com.micrantha.skouter.platform

import com.micrantha.skouter.data.clue.model.LabelResponse

expect class ImageLabelAnalyzer {

    suspend fun analyze(image: CameraImage): Result<List<LabelResponse>>
}
