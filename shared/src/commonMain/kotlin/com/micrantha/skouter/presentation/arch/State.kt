package com.micrantha.bluebell.ui.arch

import kotlinx.coroutines.flow.StateFlow

fun interface Stateful<State> {
    fun state(): StateFlow<State>
}
