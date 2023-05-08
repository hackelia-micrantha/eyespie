package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.ui.view.StatefulViewModel
import com.micrantha.bluebell.ui.view.ViewContext

class DashboardViewModel(
    viewContext: ViewContext
) : StatefulViewModel<DashboardState>(viewContext, DashboardState()) {

    init {
        store.addReducer(DashboardReducer())
            .applyEffect(DashboardEffects())
    }
}
