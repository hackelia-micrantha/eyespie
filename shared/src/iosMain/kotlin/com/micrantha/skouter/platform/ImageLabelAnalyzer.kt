package com.micrantha.skouter.platform

import com.micrantha.skouter.data.clue.model.LabelResponse

actual class ImageLabelAnalyzer {

    actual suspend fun analyze(image: CameraImage): Result<List<LabelResponse>> {

        return Result.failure(NotImplementedError())
    }
}
