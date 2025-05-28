package com.micrantha.eyespie.features.dashboard.ui

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import org.kodein.di.instance
import org.kodein.di.on

class DashboardScreenModel(
    context: ScreenContext,
    initialState: DashboardState = DashboardState()
) : MappedScreenModel<DashboardState, DashboardUiState>(
    context, initialState, DashboardEnvironment.Companion::map
) {

    private val environment: DashboardEnvironment by di.on(this).instance()

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
