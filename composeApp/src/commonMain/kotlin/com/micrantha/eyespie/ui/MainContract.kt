package com.micrantha.eyespie.ui

sealed interface MainAction {

    data object Loaded : MainAction

    data object Login : MainAction

    data object Load : MainAction

}
