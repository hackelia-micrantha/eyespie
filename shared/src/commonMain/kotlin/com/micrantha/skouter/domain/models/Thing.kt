package com.micrantha.skouter.domain.models

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.datetime.Instant

data class ThingListing(
    override val id: String,
    override val nodeId: String,
    override val createdAt: Instant,
    val name: String,
    val image: String
) : Entity, Creatable

data class Thing(
    override val id: String,
    override val nodeId: String,
    override val createdAt: Instant,
    val name: String,
    val guesses: List<Guess>,
    val image: Image,
    val clues: Clues
) : Entity, Creatable {

    data class Image(
        val fileId: String,
        val bucketId: String,
        val playerId: String,
        val data: ImageBitmap? = null
    )

    data class Guess(
        val at: Instant,
        val by: Player.Ref,
        val correct: Boolean
    )
}
