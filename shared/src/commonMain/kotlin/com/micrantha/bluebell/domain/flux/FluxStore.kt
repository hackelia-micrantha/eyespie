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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class FluxStore<State> internal constructor(
    initialState: State,
    private val stateScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined) + Job()
) : Store<State>, Store.Listener<State> {
    private val reducers = mutableListOf<Reducer<State>>()
    private val effects = mutableListOf<Effect<State>>()
    private val current = MutableStateFlow(initialState)

    override fun dispatch(action: Action) {
        current.update { state ->
            reducers.fold(state) { next, reducer -> reducer.reduce(next, action) }
        }
        stateScope.launch {
            effects.forEach { effect -> effect(action, current.value) }
        }
    }

    override fun addReducer(reducer: Reducer<State>): FluxStore<State> {
        reducers.add(reducer)
        return this
    }

    override fun applyEffect(effect: Effect<State>): FluxStore<State> {
        effects.add(effect)
        return this
    }

    override fun state(): StateFlow<State> = current.asStateFlow()

    override fun listen(block: (State) -> Unit): Store<State> {
        current.onEach(block).launchIn(stateScope)
        return this
    }
}

