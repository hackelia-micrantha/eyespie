package com.micrantha.bluebell.ui.scaffold

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.ui.navi.NavAction

data class ScaffoldState(
    val title: String? = null,
    val showBack: Boolean = false,
    val backAction: NavAction? = null,
    val actions: List<NavAction>? = null
)

sealed class ScaffoldAction : Action {
    data class SetTitle(val title: String) : ScaffoldAction()

    data class SetNavigation(val builder: ScaffoldBuilder) : ScaffoldAction()

    object Reset : ScaffoldAction()

    companion object {
        fun scaffolding(init: ScaffoldBuilder.() -> Unit) =
            ScaffoldBuilder().apply(init).build()
    }
}
