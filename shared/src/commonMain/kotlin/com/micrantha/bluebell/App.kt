package com.micrantha.bluebell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.micrantha.bluebell.domain.arch.Store
import com.micrantha.bluebell.domain.flux.Flux
import com.micrantha.bluebell.ui.scaffold.ScaffoldScreen
import com.micrantha.bluebell.ui.scaffold.rememberScaffoldStore
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.withViewContext
import org.kodein.di.DI
import org.kodein.di.compose.localDI
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.subDI

@Composable
fun <State> rememberStore(state: State): Store<State> {
    val flux: Flux by rememberInstance()
    return remember { flux.createStore(state) }
}

@Composable
fun BluebellApp(
    module: DI = localDI(),
    content: @Composable () -> Unit
) = subDI(parentDI = module, diBuilder = { import(bluebellModules()) }) {

    withViewContext {
        val screenContext by rememberInstance<ScreenContext>()

        CompositionLocalProvider(
            LocalScreenContext provides screenContext
        ) {
            val store by rememberScaffoldStore()
            val state by store.state().collectAsState()

            ScaffoldScreen(state) {
                content()
            }
        }
    }
}
