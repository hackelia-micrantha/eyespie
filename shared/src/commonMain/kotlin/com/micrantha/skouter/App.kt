package com.micrantha.skouter

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.micrantha.bluebell.BluebellApp
import com.micrantha.skouter.ui.login.LoginScreen
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.compose.subDI

@Composable
fun SkouterApp(module: DI) = subDI(parentDI = module,
    diBuilder = { import(skouterModules()) }
) {
    Navigator(LoginScreen()) { navigator ->
        subDI(diBuilder = {
            bindProvider { navigator }
        }) {
            BluebellApp {
                CurrentScreen()
            }
        }
    }
}
