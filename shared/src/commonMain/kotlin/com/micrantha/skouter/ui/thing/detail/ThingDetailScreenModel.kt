package com.micrantha.skouter.ui.thing.detail

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class ThingDetailScreenModel(
    context: ScreenContext,
    private val environment: ThingDetailEnvironment
) : MappedScreenModel<ThingDetailState, ThingDetailUiState>(
    context,
    ThingDetailState(),
    environment::map
) {
    init {
        store.addReducer(environment::reduce).applyEffect(environment)
    }
}
