package com.micrantha.eyespie.data.thing.model

@kotlinx.serialization.Serializable
data class NearbyRequest(
    val latitude: Double,
    val longitude: Double,
    val distance: Double
)
