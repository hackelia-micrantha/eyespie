package com.micrantha.bluebell

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.micrantha.bluebell.ui.scaffold.ScaffoldScreen
import com.micrantha.bluebell.ui.scaffold.Scaffolding
import com.micrantha.bluebell.ui.screen.LocalDispatcher
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.bindViewContext
import org.kodein.di.DI
import org.kodein.di.compose.localDI
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.subDI

@Composable
fun BluebellApp(
    module: DI = localDI(),
    content: @Composable () -> Unit
) = subDI(parentDI = module, diBuilder = { import(bluebellModules()) }) {

    bindViewContext {
        val screenContext by rememberInstance<ScreenContext>()

        CompositionLocalProvider(
            LocalScreenContext provides screenContext,
            LocalDispatcher provides screenContext.dispatcher
        ) {
            LayoutScreen(content)
        }
    }
}

@Composable
private fun LayoutScreen(content: @Composable () -> Unit) {
    val screen = LocalScreenContext.current.screen
    if (screen is Scaffolding) {
        ScaffoldScreen(screen, content)
    } else {
        Surface(content = content)
    }
}
