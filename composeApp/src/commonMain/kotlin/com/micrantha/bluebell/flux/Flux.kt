package com.micrantha.bluebell.flux

import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.arch.Store
import com.micrantha.bluebell.arch.StoreFactory
import kotlinx.coroutines.CoroutineScope

class Flux(
    private val dispatcher: FluxDispatcher
) : StoreFactory, Dispatcher by dispatcher {

    override fun <T> createStore(initialState: T, scope: CoroutineScope): Store<T> =
        FluxStore(initialState, scope).apply { dispatcher.register(this) }
}
