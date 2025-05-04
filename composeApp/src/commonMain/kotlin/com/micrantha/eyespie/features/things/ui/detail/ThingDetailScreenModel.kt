package com.micrantha.eyespie.features.things.ui.detail

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class ThingDetailScreenModel(
    context: ScreenContext,
    private val environment: ThingDetailEnvironment,
    initialState: ThingDetailState = ThingDetailState(),
) : MappedScreenModel<ThingDetailState, ThingDetailUiState>(
    context,
    initialState,
    ThingDetailEnvironment::map
) {
    init {
        store.addReducer(environment::reduce).applyEffect(environment)
    }
}
