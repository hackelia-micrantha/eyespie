package com.micrantha.eyespie.ui.thing.detail

import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.eyespie.domain.model.Thing
import kotlinx.serialization.Serializable

data class ThingDetailState(
    val status: UiResult<Thing> = UiResult.Default
)

data class ThingDetailUiState(
    val status: UiResult<Thing>
)

@Serializable
data class ThingDetailArg(
    val id: String
)
