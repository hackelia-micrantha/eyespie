package com.micrantha.eyespie.features.things.data.model

import com.micrantha.eyespie.domain.entities.Embedding
import com.micrantha.eyespie.features.scan.data.model.ProofData
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class ThingData(
    val id: String? = null,
    val created_at: String? = null,
    val name: String? = null,
    val imageUrl: String,
    val proof: ProofData? = null,
    val guessed: Boolean? = null,
    val created_by: String,
    val location: String? = null,
    val embedding: JsonElement,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is ThingData) return false
        return id == other.id
    }

    override fun hashCode() = id.hashCode()
}

typealias ThingRequest = ThingData
typealias ThingResponse = ThingData
typealias ThingListing = ThingData

fun Embedding.toJsonElement() =
    Json.encodeToJsonElement(this.toByteArray())

fun JsonElement.toImageEmbedding() =
    Embedding.of(*Json.decodeFromString(this.jsonPrimitive.content))
