package com.micrantha.skouter.domain.models

import kotlinx.datetime.LocalDateTime

data class Thing(
    val id: String,
    val name: String,
    val guesses: List<Guess>,
    val image: Image,
    val clues: Clues
) {
    data class Image(
        val fileId: String,
        val bucketId: String
    )

    data class Guess(
        val at: LocalDateTime,
        val by: Player.Ref,
        val correct: Boolean
    )
}
