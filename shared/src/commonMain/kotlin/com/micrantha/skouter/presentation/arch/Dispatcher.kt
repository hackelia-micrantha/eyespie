package com.micrantha.bluebell.ui.arch

typealias Dispatch = (Action) -> Unit

fun interface Dispatcher {
    fun dispatch(action: Action)

    fun interface Listener {
        fun register(dispatch: Dispatch)
    }
}
