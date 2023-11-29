package com.micrantha.skouter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.micrantha.bluebell.BluebellApp
import com.micrantha.skouter.ui.MainScreen
import com.micrantha.skouter.ui.SplashScreen
import com.micrantha.skouter.ui.component.rememberLocationTracker
import com.micrantha.skouter.ui.component.rememberPermissionsController
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.localDI
import org.kodein.di.compose.subDI

@Composable
fun SkouterApp(module: DI = localDI()) = subDI(
    parentDI = module,
    diBuilder = {
        importOnce(skouterModules())
    }
) {
    Navigator(SplashScreen()) { navigator ->

        val permissions = rememberPermissionsController()
        val locationTracker = rememberLocationTracker(permissions)

        subDI(
            diBuilder = {
                bindSingleton { navigator }
                bindSingleton { permissions }
                bindSingleton(overrides = true) { locationTracker }
            }
        ) {

            BluebellApp {
                LaunchedEffect(Unit) {
                    if (navigator.lastItem is SplashScreen) {
                        navigator.replaceAll(MainScreen())
                    }
                }

                CurrentScreen()
            }
        }
    }
}
