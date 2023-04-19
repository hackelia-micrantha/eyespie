package com.micrantha.bluebell.ui.flux

import com.micrantha.bluebell.ui.arch.Dispatcher
import com.micrantha.bluebell.ui.arch.Store
import com.micrantha.bluebell.ui.arch.StoreFactory

class Flux(
    private val dispatcher: FluxDispatcher
) : StoreFactory, Dispatcher by dispatcher {

    override fun <T> createStore(with: T): Store<T> =
        FluxStore(with).apply {
            dispatcher.register(this::dispatch)
        }
}
