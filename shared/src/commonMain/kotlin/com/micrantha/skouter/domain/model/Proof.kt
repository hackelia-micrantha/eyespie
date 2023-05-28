package com.micrantha.skouter.domain.model

data class Proof(
    val labels: List<LabelClue>? = null
)

fun Proof.asClues() = Clues(
    label = labels?.minOrNull()
)

typealias LabelProof = List<LabelClue>
