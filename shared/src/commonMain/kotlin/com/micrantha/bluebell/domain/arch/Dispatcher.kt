package com.micrantha.bluebell.domain.arch

interface Dispatch : Dispatcher {
    operator fun invoke(action: Action) = dispatch(action)
}

interface Dispatcher {

    fun dispatch(action: Action)

    suspend fun send(action: Action)

    fun interface Registry {
        fun register(dispatcher: Dispatcher)
    }

    interface Observer {
        suspend fun receive(action: Action)
    }
}
