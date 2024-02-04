package com.micrantha.skouter.data.clue.model

import com.micrantha.skouter.domain.model.Clues

data class GenerateImageRequest(
    val inputs: String
)

fun Clues.toImageRequest(): GenerateImageRequest {
    var inputs = ""

    if (this.colors?.isNotEmpty() == true) {
        inputs += this.colors.first()
    }

    if (this.labels?.isNotEmpty() == true) {
        inputs += this.labels.first().data
    }

    return GenerateImageRequest(inputs = inputs)
}
