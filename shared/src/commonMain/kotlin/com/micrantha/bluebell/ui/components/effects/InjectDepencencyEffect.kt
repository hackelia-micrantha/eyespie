package com.micrantha.bluebell.ui.components.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import org.koin.core.component.KoinComponent
import org.koin.dsl.module

@Composable
inline fun <reified T> InjectDependencyEffect(component: KoinComponent, value: T) {

    val modules = remember { listOf(module { single { value } }) }

    DisposableEffect(value) {

        component.getKoin().loadModules(modules)

        onDispose {
            component.getKoin().unloadModules(modules)
        }
    }
}
