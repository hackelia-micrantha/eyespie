package com.micrantha.skouter.ui

import com.micrantha.bluebell.domain.arch.Action

sealed interface MainAction : Action {

    object Loaded : MainAction

    object Login : MainAction

    object Load : MainAction

}
