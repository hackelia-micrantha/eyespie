package com.micrantha.skouter.domain.models

data class Location(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
    val city: String? = null,
    var region: String? = null,
    var country: String? = null,
    var accuracy: Double? = null
)
