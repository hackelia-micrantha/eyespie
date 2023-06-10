package com.micrantha.skouter.data.thing.model

import com.micrantha.skouter.data.clue.model.ProofData
import kotlinx.serialization.Serializable

@Serializable
data class ThingData(
    val id: String? = null,
    val created_at: String? = null,
    val name: String,
    val imageUrl: String,
    val proof: ProofData? = null,
    val guessed: Boolean? = null,
    val created_by: String,
    val location: String? = null
)

typealias ThingRequest = ThingData
typealias ThingResponse = ThingData
typealias ThingListing = ThingData
