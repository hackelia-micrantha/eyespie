package com.micrantha.bluebell.ui

import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.presentation.view.StatefulViewModel
import com.micrantha.skouter.presentation.view.ViewModel

class MainViewModel(
    viewContext: ViewContext
) : StatefulViewModel<MainState>(viewContext, MainState()) {

    init {
        store.addReducer { state, action ->
            when (action) {
                is MainAction.SetTitle -> state.copy(
                    title = action.title,
                    showBack = action.showBack
                )
                is MainAction.Refresh -> state.copy(
                    title = null,
                    showBack = false
                )
                is MainAction.SetRoutes -> state.copy(routes = action.routes)
                else -> state
            }
        }
    }
}
