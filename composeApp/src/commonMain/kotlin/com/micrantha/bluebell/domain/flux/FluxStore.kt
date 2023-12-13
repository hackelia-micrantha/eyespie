package com.micrantha.bluebell.domain.flux

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.arch.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet

class FluxStore<State> internal constructor(
    initialState: State,
    override val dispatchScope: CoroutineScope
) : Store<State> {
    private val reducers = mutableListOf<Reducer<State>>()
    private val current = MutableStateFlow(initialState)
    private val effects = FluxEffects<State>(dispatchScope)

    private fun update(action: Action) = current.updateAndGet { state ->
        reducers.fold(state) { next, reducer -> reducer.reduce(next, action) }
    }

    override fun dispatch(action: Action) {
        val value = update(action)
        effects.dispatch(action, value)
    }

    override suspend fun send(action: Action) {
        val value = update(action)
        effects.send(action, value)
    }

    override fun addReducer(reducer: Reducer<State>): FluxStore<State> {
        reducers.add(reducer)
        return this
    }

    override fun applyEffect(effect: Effect<State>): FluxStore<State> {
        effects.apply(effect)
        return this
    }

    override val state: StateFlow<State> = current.asStateFlow()
}

