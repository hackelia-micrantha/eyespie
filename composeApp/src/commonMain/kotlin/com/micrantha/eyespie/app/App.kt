package com.micrantha.eyespie.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.micrantha.bluebell.BluebellApp
import com.micrantha.eyespie.app.ui.MainScreen
import com.micrantha.eyespie.app.ui.SplashScreen
import com.micrantha.eyespie.core.ui.component.rememberLocationTracker
import com.micrantha.eyespie.core.ui.component.rememberPermissionsController
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.localDI
import org.kodein.di.compose.subDI
import com.micrantha.eyespie.app.module as eyespieModules

@Composable
fun EyesPieApp(module: DI = localDI()) = subDI(
    parentDI = module,
    diBuilder = {
        importOnce(eyespieModules())
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
                    navigator.replaceAll(MainScreen())
                }

                CurrentScreen()
            }
        }
    }
}
