package com.micrantha.bluebell.ui

import com.micrantha.bluebell.domain.arch.Action

typealias BackHandler = () -> Unit

sealed class MainAction : Action {
    data class SetTitle(val title: String, val showBack: Boolean = false) : MainAction()

    object Refresh : MainAction()

    companion object {
        fun setTitle(init: ScaffoldBuilder.() -> Unit) = ScaffoldBuilder().apply(init).asTitle()
    }
}

class ScaffoldBuilder {
    var title: String? = null
    var showBack: Boolean = false
    var onBack: BackHandler? = null

    fun asTitle() = title?.let { MainAction.SetTitle(it, showBack) } ?: MainAction.Refresh
}

data class MainState(
    val title: String? = null,
    val showBack: Boolean = false,
    val onBack: BackHandler? = null,
)
