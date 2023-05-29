package com.micrantha.bluebell.domain.flux

import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Store
import com.micrantha.bluebell.domain.arch.StoreFactory
import kotlinx.coroutines.CoroutineScope

class Flux(
    private val dispatcher: FluxDispatcher
) : StoreFactory, Dispatcher by dispatcher {

    override fun <T> createStore(initialState: T, scope: CoroutineScope): Store<T> =
        FluxStore(initialState, scope).apply { dispatcher.register(this::dispatch) }
}
