package com.micrantha.bluebell.domain.flux

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Effect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FluxEffects<State> internal constructor(
    // typically the screen model scope
    private val scope: CoroutineScope
) {
    private val effects = mutableListOf<Effect<State>>()

    suspend fun send(action: Action, value: State) {
        effects.forEach { effect -> effect(action, value) }
    }

    fun dispatch(action: Action, value: State) {
        scope.launch {
            send(action, value)
        }
    }


    fun apply(effect: Effect<State>) {
        effects.add(effect)
    }
}