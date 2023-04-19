package com.micrantha.skouter.presentation.view

import com.micrantha.bluebell.ui.arch.Action
import com.micrantha.bluebell.ui.arch.Dispatcher
import com.micrantha.bluebell.ui.arch.Stateful
import com.micrantha.bluebell.ui.navi.Router
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.namegame.ui.view.mapIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

expect abstract class ViewModel() {
    val viewModelScope: CoroutineScope
}

abstract class ViewContextModel<State>(
    private val viewContext: ViewContext
): ViewModel(), Dispatcher {
    override fun dispatch(action: Action) = viewContext.dispatch(action)

    internal fun router(): Router = viewContext.router
}

abstract class StatefulViewModel<State>(
    viewContext: ViewContext,
    initialState: State
) : ViewContextModel<State>(viewContext), Stateful<State> {

    protected val store = viewContext.createStore(with = initialState)

    override fun state(): StateFlow<State> = store.state()
}

typealias StateMapper<State, UiState> = (State) -> UiState

abstract class MappedViewModel<State, UiState>(
    viewContext: ViewContext,
    initialState: State,
    private val mapper: StateMapper<State, UiState>
) : ViewContextModel<State>(viewContext), Stateful<UiState> {

    protected val store = viewContext.createStore(with = initialState)

    override fun state(): StateFlow<UiState> = store.state().mapIn(viewModelScope, mapper)
}
