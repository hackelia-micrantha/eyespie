package com.micrantha.bluebell.flux

import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Effect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

class FluxEffects<State> internal constructor(
    private val scope: CoroutineScope
) {
    @Volatile
    private var effects = emptyList<Effect<State>>()

    suspend fun send(action: Action, value: State) {
        val currentEffects = effects
        currentEffects.forEach { effect -> effect(action, value) }
    }

    fun dispatch(action: Action, value: State) {
        scope.launch {
            send(action, value)
        }
    }


    fun apply(effect: Effect<State>): FluxEffects<State> {
        effects = effects.plus(effect)
        return this
    }
}