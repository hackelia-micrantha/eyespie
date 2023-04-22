package com.micrantha.bluebell.domain.arch

import kotlinx.coroutines.flow.StateFlow

fun interface Stateful<State> {
    fun state(): StateFlow<State>
}
