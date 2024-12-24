package com.micrantha.bluebell.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.micrantha.bluebell.platform.ConnectivityStatus
import kotlinx.coroutines.flow.distinctUntilChanged
import org.kodein.di.compose.rememberInstance

@Composable
fun rememberConnectivityStatus(key: Any? = Unit): State<Boolean> {
    val status by rememberInstance<ConnectivityStatus>()

    DisposableEffect(key, status) {

        status.start()

        onDispose {
            status.stop()
        }
    }
    return status.connected.distinctUntilChanged().collectAsState(status.isConnected)
}