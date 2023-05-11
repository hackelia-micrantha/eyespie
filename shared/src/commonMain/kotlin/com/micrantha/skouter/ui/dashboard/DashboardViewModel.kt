package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.ui.view.StatefulViewModel
import com.micrantha.bluebell.ui.view.ViewContext

class DashboardViewModel(
    viewContext: ViewContext,
    environment: DashboardEnvironment
) : StatefulViewModel<DashboardState>(viewContext, DashboardState()) {

    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
