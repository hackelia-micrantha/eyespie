package com.micrantha.skouter.domain.models

data class Player(
    val id: String,
    val name: Name,
    val email: String,
    val score: Score,
    val image: Image? = null,
) {
    data class Name(
        val first: String,
        val last: String,
        val nick: String,
    )

    data class Score(
        val total: Int
    )

    data class Image(
        val small: String,
        val medium: String,
        val large: String,
    )

    data class Ref(
        val id: String,
        val name: String,
    )
}
