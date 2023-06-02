package com.micrantha.skouter.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import com.micrantha.bluebell.bluebellModules
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.arch.Store
import com.micrantha.bluebell.domain.arch.StoreFactory
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.platform.Platform
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.Router.Options
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.skouter.skouterModules
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.compose.withDI

class PreviewRouter : Router {
    override fun navigateBack() = false
    override val canGoBack = false
    override val screen: Screen = object : Screen {
        @Composable
        override fun Content() {
        }
    }

    override fun <T : Screen> navigate(screen: T, options: Options) = Unit

    override val di: DI = DI.lazy {
        import(bluebellModules())
        import(skouterModules())
        bindProvider { this }
    }
}

class PreviewContext(
    context: Context,
    private val platform: Platform = Platform(context),
    override val router: PreviewRouter = PreviewRouter()
) : StoreFactory, LocalizedRepository by platform, Router by router, ScreenContext {

    override val dispatcher: Dispatcher = Dispatcher {}

    override val i18n: LocalizedRepository = platform

    override val fileSystem: FileSystem = platform

    override fun <T> createStore(initialState: T, scope: CoroutineScope): Store<T> {
        return object : Store<T> {
            override val state: StateFlow<T> = MutableStateFlow(initialState)
            override fun dispatch(action: Action) = Unit
            override fun applyEffect(effect: Effect<T>): Store<T> = this
            override fun addReducer(reducer: Reducer<T>): Store<T> = this
        }
    }
}

@Composable
fun PreviewContext(bindings: DI.MainBuilder.() -> Unit, content: @Composable () -> Unit) {

    val context = LocalContext.current

    CompositionLocalProvider(
        LocalScreenContext provides PreviewContext(context = context)
    ) {
        withDI(bindings, content)
    }
}
