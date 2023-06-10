package com.micrantha.bluebell

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import cafe.adriel.voyager.core.screen.Screen
import com.micrantha.bluebell.ui.scaffold.Scaffolding
import com.micrantha.bluebell.ui.screen.LocalDispatcher
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import com.micrantha.bluebell.ui.screen.ScreenContext
import org.kodein.di.DI
import org.kodein.di.compose.localDI
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.subDI

@Composable
fun BluebellApp(
    module: DI = localDI(),
    content: @Composable () -> Unit
) = subDI(parentDI = module, diBuilder = {
    importOnce(bluebellModules())
}) {
    val screenContext by rememberInstance<ScreenContext>()

    CompositionLocalProvider(
        LocalScreenContext provides screenContext,
        LocalDispatcher provides screenContext.dispatcher
    ) {
        val screen = LocalScreenContext.current.router.screen

        LayoutScreen(screen, content)
    }
}

@Composable
private fun LayoutScreen(screen: Screen, content: @Composable () -> Unit) {
    if (screen is Scaffolding) {
        content()
    } else {
        Surface(content = content)
    }
}
