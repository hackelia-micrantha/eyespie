package com.micrantha.bluebell.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ConnectivityStatus(private val monitor: NetworkMonitor) {

    private val connectedState = MutableStateFlow(false)

    val connected: Flow<Boolean> = connectedState

    val isConnected: Boolean = connectedState.value

    fun start() {
        monitor.startMonitoring { connectedState.value = it }
    }

    fun stop() = monitor.stopMonitoring()
}
