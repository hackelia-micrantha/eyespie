package com.micrantha.bluebell.domain.arch

typealias Dispatch = (Action) -> Unit

fun interface Dispatcher {
    fun dispatch(action: Action)

    fun interface Registry {
        fun register(dispatch: Dispatch)
    }

    interface Observer {
        suspend fun receive(action: Action)
    }
}
