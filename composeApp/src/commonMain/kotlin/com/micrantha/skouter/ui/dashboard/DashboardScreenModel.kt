package com.micrantha.skouter.ui.dashboard

import cafe.adriel.voyager.core.model.screenModelScope
import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import org.kodein.di.instance
import org.kodein.di.on

class DashboardScreenModel(
    context: ScreenContext,
    initialState: DashboardState = DashboardState()
) : MappedScreenModel<DashboardState, DashboardUiState>(
    context, initialState, DashboardEnvironment::map
) {

    private val environment: DashboardEnvironment by di.on(this).instance(arg = screenModelScope)

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
