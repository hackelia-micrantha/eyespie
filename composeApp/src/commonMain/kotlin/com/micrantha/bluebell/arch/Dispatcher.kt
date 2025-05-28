package com.micrantha.bluebell.arch

import kotlinx.coroutines.CoroutineScope

interface Dispatch : Dispatcher {
    operator fun invoke(action: Action) = dispatch(action)
}

interface Dispatcher {

    fun dispatch(action: Action)

    suspend fun send(action: Action)

    val dispatchScope: CoroutineScope

    fun interface Registry {
        fun register(dispatcher: Dispatcher)
    }

    interface Observer {
        suspend fun receive(action: Action)
    }
}
