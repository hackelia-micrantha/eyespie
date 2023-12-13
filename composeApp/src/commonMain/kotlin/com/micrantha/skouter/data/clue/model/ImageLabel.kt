package com.micrantha.skouter.data.clue.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteImageLabel(
    val confidence: Float,
    val text: String,
)

typealias ImageResponse = List<RemoteImageLabel>
