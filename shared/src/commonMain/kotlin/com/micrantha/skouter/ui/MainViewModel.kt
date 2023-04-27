package com.micrantha.skouter.ui

import com.micrantha.bluebell.ui.view.StatefulViewModel
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.MainAction.Init
import com.micrantha.skouter.ui.MainAction.Refresh
import com.micrantha.skouter.ui.MainAction.SetTitle
import com.micrantha.skouter.ui.navi.NavContext
import kotlinx.coroutines.launch

class MainViewModel(
    val viewContext: ViewContext,
    val environment: MainEnvironment
) : StatefulViewModel<MainState>(viewContext, MainState()) {

    init {
        store.addReducer { state, action ->
            when (action) {
                is Init -> state.copy(
                    isLoggedIn = action.isLoggedIn
                )
                is SetTitle -> state.copy(
                    title = action.title,
                    showBack = action.showBack
                )
                is Refresh -> state.copy(
                    title = null,
                    showBack = false
                )
                else -> state
            }
        }.applyEffect { action, state ->
            when (action) {
                is Init -> viewContext.changeNavigationContext(
                    if (state.isLoggedIn)
                        NavContext.User
                    else
                        NavContext.Init
                )
            }
        }

        viewModelScope.launch {
            dispatch(Init(environment.isLoggedIn()))
        }
    }
}
