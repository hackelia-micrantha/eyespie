package com.micrantha.bluebell.platform

import cocoapods.Reachability.*
import com.micrantha.bluebell.data.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual class ConnectivityStatus {
    private val networkStatus = MutableStateFlow(false)

    actual val isNetworkConnected: Flow<Boolean> = networkStatus

    private var reachability: Reachability? = null

    actual fun start() {
        dispatch_async(dispatch_get_main_queue()) {
            reachability = Reachability.reachabilityForInternetConnection()

            val reachableCallback = { _: Reachability? ->
                dispatch_async(dispatch_get_main_queue()) {
                    Log.d("Connected")

                    networkStatus.value = true
                }
            }
            reachability?.reachableBlock = reachableCallback

            val unreachableCallback = { _: Reachability? ->
                dispatch_async(dispatch_get_main_queue()) {
                    Log.d("Disconnected")

                    networkStatus.value = false
                }
            }
            reachability?.unreachableBlock = unreachableCallback

            reachability?.startNotifier()

            dispatch_async(dispatch_get_main_queue()) {
                networkStatus.value = reachability?.isReachable() ?: false

                Log.d("Initial reachability: ${reachability?.isReachable()}")
            }
        }
    }

    actual fun stop() {
        reachability?.stopNotifier()
    }
}
