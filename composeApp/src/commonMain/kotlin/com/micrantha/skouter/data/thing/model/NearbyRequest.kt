package com.micrantha.skouter.data.thing.model

@kotlinx.serialization.Serializable
data class NearbyRequest(
    val latitude: Double,
    val longitude: Double,
    val distance: Double
)
