package com.micrantha.bluebell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
        LocalDispatcher provides screenContext.dispatcher,
        content = content
    )
}
