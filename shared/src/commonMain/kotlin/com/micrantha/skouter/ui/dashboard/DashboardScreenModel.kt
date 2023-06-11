package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class DashboardScreenModel(
    context: ScreenContext,
    environment: DashboardEnvironment,
    initialState: DashboardState = DashboardState()
) : MappedScreenModel<DashboardState, DashboardUiState>(
    context, initialState, environment::map
) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
