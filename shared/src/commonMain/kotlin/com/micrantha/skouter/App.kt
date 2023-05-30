package com.micrantha.skouter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.micrantha.bluebell.BluebellApp
import com.micrantha.skouter.ui.MainScreen
import com.micrantha.skouter.ui.component.rememberImageLoader
import com.seiko.imageloader.LocalImageLoader
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy.Best
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.media.compose.BindMediaPickerEffect
import dev.icerock.moko.media.compose.rememberMediaPickerControllerFactory
import dev.icerock.moko.media.picker.MediaPickerController
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.subDI

@Composable
fun SkouterApp(module: DI) = subDI(parentDI = module,
    diBuilder = { import(skouterModules()) }
) {
    Navigator(MainScreen()) { navigator ->

        val permissions = rememberPermissionsController()
        val mediaPicker = rememberMediaPickerController(permissions)
        val locationTracker = rememberLocationTracker(permissions)
        val imageLoader = rememberImageLoader()

        subDI(diBuilder = {
            bindSingleton { navigator }
            bindSingleton { permissions }
            bindSingleton { mediaPicker }
            bindSingleton(overrides = true) { locationTracker }
        }) {

            CompositionLocalProvider(
                LocalImageLoader provides imageLoader,
            ) {
                BluebellApp {
                    CurrentScreen()
                }
            }
        }
    }
}

@Composable
fun rememberPermissionsController(): PermissionsController {
    val permissionsFactory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController: PermissionsController =
        remember(permissionsFactory) { permissionsFactory.createPermissionsController() }

    BindEffect(permissionsController)

    return permissionsController
}

@Composable
fun rememberMediaPickerController(permissionsController: PermissionsController): MediaPickerController {
    val mediaFactory = rememberMediaPickerControllerFactory()
    val mediaPicker =
        remember(mediaFactory) { mediaFactory.createMediaPickerController(permissionsController) }

    BindMediaPickerEffect(mediaPicker)

    return mediaPicker
}

@Composable
fun rememberLocationTracker(permissionsController: PermissionsController): LocationTracker {
    // TODO: accuracy should by dynamic and configurable
    val factory = rememberLocationTrackerFactory(Best)
    val tracker = remember(factory) { factory.createLocationTracker(permissionsController) }

    BindLocationTrackerEffect(tracker)

    return tracker
}
