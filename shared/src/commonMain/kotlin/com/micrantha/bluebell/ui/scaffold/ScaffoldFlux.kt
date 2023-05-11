package com.micrantha.bluebell.ui.scaffold

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.ui.scaffold.ScaffoldAction.Reset
import com.micrantha.bluebell.ui.scaffold.ScaffoldAction.SetNavigation
import com.micrantha.bluebell.ui.scaffold.ScaffoldAction.SetTitle

fun scaffoldReducer(state: ScaffoldState, action: Action) = when (action) {

    is SetTitle -> state.copy(
        title = action.title,
    )
    is SetNavigation -> state.copy(
        title = action.builder.title ?: state.title,
        backAction = (action.builder.onBack ?: state.backAction)?.toggle(
            action.builder.showBack ?: state.backAction?.enabled ?: false
        ),
        actions = action.builder.actions ?: state.actions
    )
    is Reset -> state.copy(
        actions = null,
        title = null,
        backAction = null,
    )
    else -> state
}
