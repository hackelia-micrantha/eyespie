package com.micrantha.skouter.ui.thing.detail

import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.model.Thing

data class ThingDetailState(
    val status: UiResult<Thing> = UiResult.Default
)

data class ThingDetailUiState(
    val status: UiResult<Thing>
)
