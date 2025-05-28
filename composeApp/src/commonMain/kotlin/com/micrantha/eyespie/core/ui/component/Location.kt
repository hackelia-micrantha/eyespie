package com.micrantha.eyespie.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.micrantha.eyespie.domain.repository.LocationRepository
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy.Best
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.launch
import org.kodein.di.compose.rememberInstance

@Composable
fun LocationEnabledEffect(key: Any? = Unit) {
    val scope = rememberCoroutineScope()
    val repository by rememberInstance<LocationRepository>()

    DisposableEffect(key, repository) {
        scope.launch {
            repository.start()
        }

        onDispose {
            repository.stop()
        }
    }
}


@Composable
fun rememberLocationTracker(permissionsController: PermissionsController): LocationTracker {
    // TODO: accuracy should by dynamic and configurable
    val factory = rememberLocationTrackerFactory(Best)
    val tracker = remember(factory) { factory.createLocationTracker(permissionsController) }

    BindLocationTrackerEffect(tracker)

    return tracker
}
