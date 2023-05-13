package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenMappedModel

class DashboardScreenModel(
    screenContext: ScreenContext,
    environment: DashboardEnvironment,
    initialState: DashboardState = DashboardState()
) : ScreenMappedModel<DashboardState, DashboardUiState>(
    screenContext, initialState, environment::map
) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
