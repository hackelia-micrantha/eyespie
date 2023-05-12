package com.micrantha.bluebell.ui.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Stateful
import com.micrantha.bluebell.domain.arch.Store
import com.micrantha.bluebell.domain.flux.Flux
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.ui.components.Router
import kotlinx.coroutines.flow.StateFlow
import org.kodein.di.DIAware
import org.kodein.di.instance

/**
 * contextual view model knows about platform, routing, localization, and global dispatching
 */
abstract class ScreenContextModel(
    protected val screenContext: ScreenContext
) : Dispatcher by screenContext.dispatcher, ScreenModel {

    protected val router: Router = screenContext

    protected val i18n: LocalizedRepository = screenContext.i18n
}

private fun <State> DIAware.screenModelStore(state: State): Lazy<Store<State>> {
    val flux by instance<Flux>()
    return lazy { flux.createStore(state) }
}

/**
 * View model with a store that bypasses global dispatcher with local store dispatch by default.
 */
abstract class ScreenStoreModel<State>(
    screenContext: ScreenContext,
    initialState: State
) : ScreenContextModel(screenContext), DIAware by screenContext {
    protected val store: Store<State> by screenModelStore(initialState)
}

/**
 * Stateful view model with a store
 */
abstract class ScreenStatefulModel<State>(
    screenContext: ScreenContext,
    initialState: State
) : ScreenStoreModel<State>(screenContext, initialState), Stateful<State> {
    override fun state(): StateFlow<State> = store.state()
}

typealias StateMapper<State, UiState> = (State) -> UiState

/**
 * Stateful view model that maps between internal state and ui state.
 */
abstract class ScreenMappedModel<State, UiState>(
    screenContext: ScreenContext,
    initialState: State,
    private val mapper: StateMapper<State, UiState>
) : ScreenStoreModel<State>(screenContext, initialState), Stateful<UiState> {
    override fun state(): StateFlow<UiState> = store.state().mapIn(coroutineScope, mapper)
}
