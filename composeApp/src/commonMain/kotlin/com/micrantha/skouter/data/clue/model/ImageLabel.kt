package com.micrantha.skouter.data.clue.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    val confidence: Float,
    val text: String,
)
