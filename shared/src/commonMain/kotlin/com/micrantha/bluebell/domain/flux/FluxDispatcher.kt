package com.micrantha.bluebell.domain.flux

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.arch.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class FluxDispatcher internal constructor(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default) + Job()
) : Dispatcher, Dispatcher.Registry, Dispatcher.Observer {
    private val actions = MutableSharedFlow<Action>()

    override fun register(dispatch: Dispatch) {
        actions.onEach(dispatch)
            .catch { Log.e(message = "registered dispatch", tag = "dispatcher", throwable = it) }
            .launchIn(scope)
    }

    override suspend fun receive(action: Action) {
        actions.first { action == it }
    }

    override fun dispatch(action: Action) {
        Log.d(action)
        scope.launch {
            actions.emit(action)
        }
    }

    private fun Log.d(action: Action) = d("action: $action", tag = "dispatcher")
}
