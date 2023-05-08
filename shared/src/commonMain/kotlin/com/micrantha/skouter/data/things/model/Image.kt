package com.micrantha.skouter.data.things.model

import com.micrantha.skouter.domain.models.Thing
import kotlinx.serialization.Serializable

@Serializable
data class ImageJson(
    val fileId: String,
    val bucketId: String,
    val playerId: String,
)

val Thing.Image.path: String
    get() = "${playerId}/${fileId}"
