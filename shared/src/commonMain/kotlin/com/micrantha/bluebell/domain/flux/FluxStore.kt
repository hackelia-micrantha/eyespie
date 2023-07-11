package com.micrantha.bluebell.domain.flux

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.arch.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class FluxStore<State> internal constructor(
    initialState: State,
    private val effectScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined) + Job()
) : Store<State> {
    private val reducers = mutableListOf<Reducer<State>>()
    private val effects = mutableListOf<Effect<State>>()
    private val current = MutableStateFlow(initialState)

    override fun dispatch(action: Action) {
        val value = current.updateAndGet { state ->
            reducers.fold(state) { next, reducer -> reducer.reduce(next, action) }
        }

        effectScope.launch {
            effects.forEach { effect -> effect(action, value) }
        }
    }

    override suspend fun invoke(action: Action) {
        val value = current.updateAndGet { state ->
            reducers.fold(state) { next, reducer -> reducer.reduce(next, action) }
        }

        effects.forEach { effect -> effect(action, value) }
    }

    override fun addReducer(reducer: Reducer<State>): FluxStore<State> {
        reducers.add(reducer)
        return this
    }

    override fun applyEffect(effect: Effect<State>): FluxStore<State> {
        effects.add(effect)
        return this
    }

    override val state: StateFlow<State> = current.asStateFlow()
}

