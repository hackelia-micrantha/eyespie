package com.micrantha.bluebell.domain.arch

interface Action

fun interface ReducerStore<State> {
    fun addReducer(reducer: Reducer<State>): Store<State>
    operator fun plus(reducer: Reducer<State>) = addReducer(reducer)
}

fun interface EffectedStore<State> {
    fun applyEffect(effect: Effect<State>): Store<State>

    operator fun plus(effect: Effect<State>) = applyEffect(effect)
}

interface Store<State> : Stateful<State>, ReducerStore<State>, EffectedStore<State>, Dispatcher {
    fun register(): Store<State>

    fun interface Listener<State> {
        fun listen(block: (State) -> Unit): Store<State>
    }
}

interface StoreFactory {
    fun <T> createStore(state: T): Store<T>
}
