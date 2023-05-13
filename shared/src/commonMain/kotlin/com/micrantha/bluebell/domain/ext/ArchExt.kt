package com.micrantha.bluebell.domain.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Store
import com.micrantha.bluebell.domain.flux.Flux
import org.kodein.di.compose.rememberInstance

fun Dispatcher.dispatch(vararg actions: Action) = actions.forEach { dispatch(it) }

@Composable
fun <State> rememberStore(state: State): Store<State> {
    val flux: Flux by rememberInstance()
    return remember { flux.createStore(state) }
}

