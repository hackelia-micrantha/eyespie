package com.micrantha.skouter.ui

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult

sealed class MainAction : Action {

    data class Loaded(val isLoggedIn: Boolean) : MainAction()

    object Load : MainAction()

}

data class MainState(
    val isLoggedIn: Boolean = false,
    val status: UiResult<Unit> = UiResult.Default
)
