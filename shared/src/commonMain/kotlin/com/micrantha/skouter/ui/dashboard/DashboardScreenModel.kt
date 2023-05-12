package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenStatefulModel

class DashboardScreenModel(
    screenContext: ScreenContext,
    environment: DashboardEnvironment
) : ScreenStatefulModel<DashboardState>(screenContext, DashboardState()) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
