package com.micrantha.bluebell.app

import com.micrantha.bluebell.app.navi.NavAction

data class ScaffoldingState(
    val title: String? = null,
    val actions: List<NavAction>? = null,
    val backAction: NavAction? = null,
    val showBack: Boolean = true,
    val canGoBack: Boolean? = null,
    val floatingActionButton: NavAction? = null
)

sealed interface Scaffolding {
    data class Title(val title: String) : Scaffolding
    data class Actions(val actions: List<NavAction>) : Scaffolding
    data class BackAction(val action: NavAction) : Scaffolding
    data class ShowBack(val showBack: Boolean) : Scaffolding
    data class CanGoBack(val canGoBack: Boolean) : Scaffolding
    data class FloatingActionButton(val action: NavAction) : Scaffolding
}
