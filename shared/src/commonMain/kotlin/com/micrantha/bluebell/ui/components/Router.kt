package com.micrantha.bluebell.ui.components

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.core.stack.StackEvent.Idle
import cafe.adriel.voyager.core.stack.StackEvent.Pop
import cafe.adriel.voyager.core.stack.StackEvent.Push
import cafe.adriel.voyager.core.stack.StackEvent.Replace
import com.micrantha.bluebell.ui.components.Router.Options.None

typealias RouterEvent = StackEvent

val RouterEvent.isBackOrIdle: Boolean
    get() = when (this) {
        Pop, Idle -> true
        Push, Replace -> false
    }


interface Router {
    fun navigateBack(): Boolean

    val canGoBack: Boolean

    fun <T : Screen> navigate(screen: T, options: Options = None)

    val isBackOrIdle: Boolean

    enum class Options {
        None,
        Replace,
        Reset
    }
}
