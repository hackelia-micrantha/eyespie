package com.micrantha.skouter.domain.model

data class Proof(
    val labels: List<LabelClue>? = null,
    val location: LocationClue? = null,
)

fun Proof.asClues() = Clues(
    label = labels?.minOrNull(),
    location = location
)

typealias LabelProof = List<LabelClue>
