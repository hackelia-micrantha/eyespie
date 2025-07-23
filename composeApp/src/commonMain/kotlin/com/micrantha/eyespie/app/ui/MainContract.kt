package com.micrantha.eyespie.app.ui

sealed interface MainAction {
    data object Load : MainAction
}
