package com.micrantha.eyespie.app.ui

sealed interface MainAction {

    data object Loaded : MainAction

    data object Login : MainAction

    data object Load : MainAction

}
