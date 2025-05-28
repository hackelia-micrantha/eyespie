package com.micrantha.eyespie.features.things.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NearbyRequest(
    val latitude: Double,
    val longitude: Double,
    val distance: Double
)
