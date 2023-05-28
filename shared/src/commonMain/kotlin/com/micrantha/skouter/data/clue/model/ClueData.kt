package com.micrantha.skouter.data.clue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClueData

@Serializable
data class ProofData(
    val labels: List<LabelClueData>? = null
)

@Serializable
@SerialName("label")
data class LabelClueData(
    val data: String,
    val confidence: Float,
) : ClueData
