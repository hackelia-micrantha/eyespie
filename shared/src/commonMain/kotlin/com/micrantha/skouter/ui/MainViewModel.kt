package com.micrantha.skouter.ui

import com.micrantha.bluebell.ui.view.StatefulViewModel
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.MainAction.CheckedLogin
import com.micrantha.skouter.ui.MainAction.Refresh
import com.micrantha.skouter.ui.MainAction.SetTitle
import kotlinx.coroutines.launch

class MainViewModel(
    val viewContext: ViewContext,
    private val environment: MainEnvironment
) : StatefulViewModel<MainState>(viewContext, MainState()) {

    init {
        store.addReducer { state, action ->
            when (action) {
                is CheckedLogin -> state.copy(
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
        }

        viewModelScope.launch {
            dispatch(MainAction.CheckedLogin(environment.isLoggedIn()))
        }
    }
}
