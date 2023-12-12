package com.micrantha.bluebell.platform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

expect class ConnectivityStatus {
    val isNetworkConnected: Flow<Boolean>
    fun start()
    fun stop()
}

fun ConnectivityStatus.getStatus(success: (Boolean) -> Unit) {
    CoroutineScope(Dispatchers.Default).launch {
        isNetworkConnected.collect { status ->
            withContext(Dispatchers.Main) {
                success(status)
            }
        }
    }
}
