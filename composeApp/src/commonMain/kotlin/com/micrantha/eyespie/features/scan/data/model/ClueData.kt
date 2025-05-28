package com.micrantha.eyespie.features.scan.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClueData

@Serializable
data class ProofData(
    val labels: List<LabelClueData>? = null,
    val colors: List<String>? = null,
    val location: LocationClueData? = null,
)

@Serializable
@SerialName("label")
data class LabelClueData(
    val data: String,
    val confidence: Float,
) : ClueData

@Serializable
@SerialName("location")
data class LocationClueData(
    val name: String? = null,
    val city: String? = null,
    var region: String? = null,
    var country: String? = null,
    var accuracy: Float? = null,
) : ClueData
