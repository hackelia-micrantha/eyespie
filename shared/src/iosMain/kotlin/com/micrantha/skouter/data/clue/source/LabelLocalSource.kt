package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.data.clue.model.LabelResponse

actual class LabelLocalSource {

    actual suspend fun analyze(data: ByteArray, rotation: Int): Result<List<LabelResponse>> {
        val image = UIImage.fromUri(path.toFile().toUri())
        val image = VisionImage(image)
        visionImage.orientation = image.imageOrientation

        return Result.failure(NotImplementedError())
    }
}
