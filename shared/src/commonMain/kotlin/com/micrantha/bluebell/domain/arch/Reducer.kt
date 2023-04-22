package com.micrantha.bluebell.domain.arch

typealias Reducer<T> = (state: T, action: Action) -> T

fun <State> combineReducers(vararg values: Reducer<State>): Reducer<State> = { state, action ->
    values.fold(state) { next, reducer ->
        reducer(next, action)
    }
}
