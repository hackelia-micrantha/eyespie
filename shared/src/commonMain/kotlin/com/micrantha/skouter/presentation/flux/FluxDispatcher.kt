package com.micrantha.bluebell.ui.flux

import com.micrantha.bluebell.ui.arch.Action
import com.micrantha.bluebell.ui.arch.Dispatch
import com.micrantha.bluebell.ui.arch.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class FluxDispatcher internal constructor(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default) + Job()
) : Dispatcher, Dispatcher.Listener {
    private val actions = MutableSharedFlow<Action>()

    override fun register(dispatch: Dispatch) {
        actions.onEach { dispatch(it) }.launchIn(scope)
    }

    override fun dispatch(action: Action) {
        scope.launch {
            actions.emit(action)
        }
    }
}
