package com.micrantha.bluebell.ui.navi

import androidx.compose.runtime.Composable
import com.chrynan.navigation.NavigationContext
import com.micrantha.bluebell.data.err.fail
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.bluebell.ui.view.ViewContextModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.collections.set

interface Route {
    val path: String
}

typealias RouteContext = NavigationContext<Route>

typealias Screen<T> = @Composable (T) -> Unit

typealias RouteRenderer = @Composable (ViewContext, Array<out Any>?) -> Unit

internal typealias MappedRoutes = Map<Route, RouteRenderer>

class RouteBuilder : KoinComponent {
    var initialContext: RouteContext? = null

    private val routedScreens = mutableMapOf<Route, RouteRenderer>()

    internal inline infix fun <State, reified T : ViewContextModel<State>> Route.to(
        noinline screen: Screen<T>
    ) {
        routedScreens[this] = { context, params ->
            if (params == null) {
                screen(get { parametersOf(context) })
            } else {
                screen(get { parametersOf(context, *params) })
            }
        }
    }

    fun build() = NavigationRoutes(
        initialContext = initialContext
            ?: fail("no initial route context configured"),
        routedScreens
    )
}

fun routes(builder: RouteBuilder.() -> Unit) = RouteBuilder().apply(builder)
