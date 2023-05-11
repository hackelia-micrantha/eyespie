package com.micrantha.skouter.ui

import com.micrantha.bluebell.ui.view.StatefulViewModel
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.MainAction.Load
import com.micrantha.skouter.ui.MainAction.Loaded
import com.micrantha.skouter.ui.navi.NavContext

class MainViewModel(
    viewContext: ViewContext,
    private val environment: MainEnvironment
) : StatefulViewModel<MainState>(viewContext, MainState()) {

    init {
        store.register().addReducer(environment::reduce).applyEffect { action, _ ->
            when (action) {
                is Load -> dispatch(
                    Loaded(environment.isLoggedIn())
                )
                is Loaded -> viewContext.changeNavigationContext(
                    if (action.isLoggedIn)
                        NavContext.User
                    else
                        NavContext.Init
                )
            }
        }
    }

    override fun onScreenActive() {
        dispatch(Load)
    }
}
