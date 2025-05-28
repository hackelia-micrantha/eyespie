package com.micrantha.bluebell.arch

import kotlinx.coroutines.flow.StateFlow

interface Stateful<State> {
    val state: StateFlow<State>
}

fun interface StateMapper<InState, OutState> {
    fun map(state: InState): OutState
}
