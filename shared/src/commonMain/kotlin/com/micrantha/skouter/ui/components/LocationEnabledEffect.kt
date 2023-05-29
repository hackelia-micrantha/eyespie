package com.micrantha.skouter.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.launch
import org.kodein.di.compose.rememberInstance

@Composable
fun LocationEnabledEffect(key: Any? = null) {
    val scope = rememberCoroutineScope()
    val tracker by rememberInstance<LocationTracker>()

    DisposableEffect(key) {
        scope.launch {
            tracker.startTracking()
        }

        onDispose {
            tracker.stopTracking()
        }
    }
}
