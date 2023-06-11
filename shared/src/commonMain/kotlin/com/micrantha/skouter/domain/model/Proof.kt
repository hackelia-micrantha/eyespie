package com.micrantha.skouter.domain.model

data class Proof(
    val labels: Set<LabelClue>? = null,
    val location: LocationClue? = null, // TODO: Geofence
    val colors: Set<ColorClue>? = null,
    val objects: Set<DetectClue>? = null,
)

fun Proof.asClues() = Clues(
    label = labels?.minOrNull(),
    location = location,
    color = colors?.minOrNull(),
    detect = objects?.minOrNull()
)

typealias LabelProof = Set<LabelClue>

typealias ColorProof = Set<ColorClue>

typealias LocationProof = LocationClue

typealias DetectProof = Set<DetectClue>

