package com.micrantha.bluebell.ui.view

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Stateful
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import com.micrantha.bluebell.ui.components.ScreenVisibility
import com.micrantha.bluebell.ui.navi.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

/**
 * the basic custom view model.
 * maps to architecture components on android
 */
expect abstract class ViewModel() : ScreenVisibility {
    val viewModelScope: CoroutineScope
}

/**
 * contextual view model knows about platform, routing, localization, and global dispatching
 */
abstract class ViewContextModel<State>(
    val viewContext: ViewContext
) : ViewModel(), LocalizedRepository by viewContext, Dispatcher by viewContext {

    internal val router: Router = viewContext

    internal fun string(str: LocalizedString) = viewContext.resource(str)
}

/**
 * View model with a store that bypasses global dispatcher with local store dispatch by default.
 */
abstract class StoreViewModel<State>(
    viewContext: ViewContext,
    initialState: State,
) : ViewContextModel<State>(viewContext) {

    protected val store = viewContext.createStore(state = initialState)

    override fun dispatch(action: Action) = store.dispatch(action)
}

/**
 * Stateful view model with a store
 */
abstract class StatefulViewModel<State>(
    viewContext: ViewContext,
    initialState: State,
) : StoreViewModel<State>(viewContext, initialState), Stateful<State> {
    override fun state(): StateFlow<State> = store.state()
}

typealias StateMapper<State, UiState> = (State) -> UiState

/**
 * Stateful view model that maps between internal state and ui state.
 */
abstract class MappedViewModel<State, UiState>(
    viewContext: ViewContext,
    initialState: State,
    private val mapper: StateMapper<State, UiState>
) : StoreViewModel<State>(viewContext, initialState), Stateful<UiState> {


    override fun state(): StateFlow<UiState> = store.state().mapIn(viewModelScope, mapper)
}
