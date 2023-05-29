package com.micrantha.skouter.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.micrantha.skouter.domain.repository.LocationRepository
import kotlinx.coroutines.launch
import org.kodein.di.compose.rememberInstance

@Composable
fun LocationEnabledEffect(key: Any? = Unit) {
    val scope = rememberCoroutineScope()
    val repository by rememberInstance<LocationRepository>()

    DisposableEffect(key) {
        scope.launch {
            repository.start()
        }

        onDispose {
            repository.stop()
        }
    }
}
