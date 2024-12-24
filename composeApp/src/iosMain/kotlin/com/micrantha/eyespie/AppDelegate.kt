package com.micrantha.eyespie

import com.micrantha.bluebell.platform.NetworkMonitor
import com.micrantha.bluebell.platform.PlatformConfigDelegate

class AppDelegate(
    val networkMonitor: NetworkMonitor,
    val appConfig: PlatformConfigDelegate
)