package com.micrantha.bluebell.domain.flux

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class FluxDispatcher internal constructor(
    override val dispatchScope: CoroutineScope = CoroutineScope(Dispatchers.Default) + Job()
) : Dispatcher, Dispatcher.Registry {
    private val actions = MutableSharedFlow<Action>()

    override fun register(dispatcher: Dispatcher) {
        actions.onEach(dispatcher::send)
            .catch { log.e { "registered dispatch failed: ${it.message}" } }
            .launchIn(dispatchScope)
    }

    override fun dispatch(action: Action) {
        log.d { "action: $action" }
        dispatchScope.launch {
            actions.emit(action)
        }
    }

    override suspend fun send(action: Action) {
        actions.emit(action)
    }

    companion object {
        private val log = Log.withTag("dispatcher")
    }
}
