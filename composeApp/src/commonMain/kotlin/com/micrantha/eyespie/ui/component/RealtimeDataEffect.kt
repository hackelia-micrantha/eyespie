package com.micrantha.eyespie.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.micrantha.eyespie.domain.repository.RealtimeRepository
import kotlinx.coroutines.launch
import org.kodein.di.compose.rememberInstance

@Composable
fun RealtimeDataEnabledEffect(key: Any? = Unit) {
    val repository by rememberInstance<RealtimeRepository>()
    val scope = rememberCoroutineScope()

    DisposableEffect(key) {

        scope.launch {
            repository.start()
        }

        onDispose {
            repository.stop()
        }
    }
}
