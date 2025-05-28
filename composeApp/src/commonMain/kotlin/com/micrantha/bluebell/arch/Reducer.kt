package com.micrantha.bluebell.arch

fun interface Reducer<T> {
    fun reduce(state: T, action: Action): T
}

fun <State> combineReducers(vararg values: Reducer<State>) =
    Reducer<State> { state, action ->
        values.fold(state) { next, reducer ->
            reducer.reduce(next, action)
        }
    }
