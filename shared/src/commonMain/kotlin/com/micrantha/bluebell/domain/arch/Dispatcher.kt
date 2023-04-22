package com.micrantha.bluebell.domain.arch

typealias Dispatch = (Action) -> Unit

fun interface Dispatcher {
    fun dispatch(action: Action)

    fun interface Listener {
        fun register(dispatch: Dispatch)
    }
}
