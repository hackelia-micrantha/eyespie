package com.micrantha.skouter.domain.model

import com.micrantha.skouter.platform.ImageEmbedding
import kotlinx.datetime.Instant

data class Thing(
    override val id: String,
    override val createdAt: Instant,
    val createdBy: Player.Ref,
    val name: String?,
    val guessed: Boolean,
    val guesses: List<Guess>,
    val imageUrl: String,
    val clues: Clues,
    val location: Location.Point
) : Entity, Creatable {

    data class Guess(
        val at: Instant,
        val by: Player.Ref,
        val correct: Boolean
    )

    data class Listing(
        override val id: String,
        val nodeId: String,
        override val createdAt: Instant,
        val name: String?,
        val guessed: Boolean,
        val imageUrl: String
    ) : Entity, Creatable

    data class Match(
        override val id: String,
        val image: ImageEmbedding,
        val similarity: Float
    ) : Entity
}

typealias ThingList = List<Thing.Listing>
typealias ThingMatches = List<Thing.Match>
