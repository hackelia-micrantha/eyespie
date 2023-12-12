package com.micrantha.bluebell.platform

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.data.d
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

actual class ConnectivityStatus(private val context: Context) {
    private val networkStatus = MutableStateFlow(false)

    actual val isNetworkConnected: Flow<Boolean> = networkStatus

    private var connectivityManager: ConnectivityManager? = null

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Log.d("Connectivity status", "Network available")
            networkStatus.value = true
        }

        override fun onLost(network: Network) {
            Log.d("Connectivity status", "Network lost")
            networkStatus.value = false
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            val isConnected =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            Log.d(
                "Connectivity status", "Network status: ${
                    if (isConnected) {
                        "Connected"
                    } else {
                        "Disconnected"
                    }
                }"
            )

            networkStatus.value = isConnected
        }
    }

    actual fun start() {
        try {
            if (connectivityManager == null) {
                connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            }

            connectivityManager?.registerDefaultNetworkCallback(networkCallback)

            val currentNetwork = connectivityManager?.activeNetwork

            if (currentNetwork == null) {
                networkStatus.value = false

                Log.d("Connectivity status", "Disconnected")
            }

            Log.d("Connectivity status", "Started")
        } catch (e: Exception) {
            Log.d(
                tag = "Connectivity status",
                throwable = e,
                messageString = "Failed to start: ${e.message.toString()}"
            )
            networkStatus.value = false
        }
    }

    actual fun stop() {
        connectivityManager?.unregisterNetworkCallback(networkCallback)
        Log.d("Connectivity status", "Stopped")
    }
}
