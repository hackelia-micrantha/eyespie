package com.micrantha.bluebell.ui

import com.micrantha.bluebell.ui.view.StatefulViewModel
import com.micrantha.bluebell.ui.view.ViewContext

class MainViewModel(
    val viewContext: ViewContext
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
                else -> state
            }
        }
    }
}
