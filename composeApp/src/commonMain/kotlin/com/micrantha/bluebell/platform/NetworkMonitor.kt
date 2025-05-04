package com.micrantha.bluebell.platform

interface NetworkMonitor {
    fun startMonitoring(onUpdate: (Boolean) -> Unit)
    fun stopMonitoring()
}
