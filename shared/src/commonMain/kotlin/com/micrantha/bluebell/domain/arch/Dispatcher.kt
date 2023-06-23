package com.micrantha.bluebell.domain.arch

import kotlinx.coroutines.flow.Flow

typealias Dispatch = (Action) -> Unit

fun interface Dispatcher {
    fun dispatch(action: Action)

    fun interface Registry {
        fun register(dispatch: Dispatch)
    }

    fun interface Observer {
        fun actions(): Flow<Action>
    }
}
