package com.micrantha.skouter

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.micrantha.bluebell.BluebellApp
import com.micrantha.skouter.ui.MainScreen
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.compose.subDI

@Composable
fun SkouterApp(module: DI) = subDI(parentDI = module,
    diBuilder = { import(skouterModules()) }
) {
    Navigator(MainScreen()) { navigator ->
        subDI(diBuilder = {
            bindProvider { navigator }
        }) {
            BluebellApp {
                CurrentScreen()
            }
        }
    }
}
