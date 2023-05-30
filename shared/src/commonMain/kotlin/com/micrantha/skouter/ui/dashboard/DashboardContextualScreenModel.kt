package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class DashboardContextualScreenModel(
    screenContext: ScreenContext,
    environment: DashboardEnvironment,
    initialState: DashboardState = DashboardState()
) : MappedScreenModel<DashboardState, DashboardUiState>(
    screenContext, initialState, environment::map
) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
