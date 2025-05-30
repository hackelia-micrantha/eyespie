package com.micrantha.eyespie.core.data.system.model

import kotlinx.serialization.Serializable

@Serializable
data class GeocodeResponse(
    val category: String? = null,
    val type: String? = null,
    val importance: Double? = null,
    val display_name: String? = null,
    val name: String? = null,
    val address: Address? = null
) {
    @Serializable
    data class Address(
        val road: String? = null,
        val village: String? = null,
        val state_district: String? = null,
        val state: String? = null,
        val district: String? = null,
        val street: String? = null,
        val country: String? = null,
        val postcode: String? = null,
    )
}
