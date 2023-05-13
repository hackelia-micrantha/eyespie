package com.micrantha.skouter.data.thing.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageJson(
    val fileId: String,
    val bucketId: String,
    val playerId: String,
)
