package com.micrantha.bluebell.domain.flux

import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Store
import com.micrantha.bluebell.domain.arch.StoreFactory

class Flux(
    private val dispatcher: FluxDispatcher
) : StoreFactory, Dispatcher by dispatcher {

    fun <T> register(store: FluxStore<T>) = dispatcher.register(store::dispatch)

    override fun <T> createStore(state: T): Store<T> = FluxStore(state, this).apply { register() }
}
