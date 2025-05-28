package com.micrantha.eyespie.features.scan.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteImageLabel(
    val confidence: Float,
    val text: String,
)

typealias ImageResponse = List<RemoteImageLabel>
