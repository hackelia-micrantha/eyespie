package com.micrantha.skouter.domain.model

data class Proof(
    val labels: Set<LabelClue>? = null,
    val location: LocationClue? = null, // TODO: Geofence
    val colors: Set<ColorClue>? = null
)

typealias LabelProof = Set<LabelClue>

typealias ColorProof = Set<ColorClue>

typealias LocationProof = LocationClue

typealias DetectProof = Set<DetectClue>

typealias SegmentProof = List<SegmentClue>

