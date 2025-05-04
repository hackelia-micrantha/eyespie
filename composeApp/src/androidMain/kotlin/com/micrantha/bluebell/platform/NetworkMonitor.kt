package com.micrantha.bluebell.platform

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.data.d

class AndroidNetworkMonitor(context: Context) : NetworkMonitor {
    private var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var networkCallback: Callback? = null

    inner class Callback(private var onUpdate: (Boolean) -> Unit) :
        ConnectivityManager.NetworkCallback() {

        fun update(onUpdate: (Boolean) -> Unit): Callback {
            this.onUpdate = onUpdate
            return this
        }

        override fun onAvailable(network: Network) {
            Log.d("Connectivity status", "Network available")
            onUpdate(true)
        }

        override fun onLost(network: Network) {
            Log.d("Connectivity status", "Network lost")
            onUpdate(false)
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

            onUpdate(isConnected)
        }
    }

    override fun startMonitoring(onUpdate: (Boolean) -> Unit) {
        try {

            if (networkCallback == null) {
                Callback(onUpdate).apply {
                    networkCallback = this
                    connectivityManager.registerDefaultNetworkCallback(this)
                }
            } else {
                networkCallback?.update(onUpdate)
            }

            if (connectivityManager.activeNetwork == null) {
                onUpdate(false)
                Log.d("Connectivity status", "Disconnected")
            }

            Log.d("Connectivity status", "Started")
        } catch (e: Exception) {
            Log.d(
                tag = "Connectivity status",
                throwable = e,
                messageString = "Failed to start: ${e.message.toString()}"
            )
            onUpdate(false)
        }
    }

    override fun stopMonitoring() {
        networkCallback?.let { connectivityManager.unregisterNetworkCallback(it) }
        networkCallback = null
        Log.d("Connectivity status", "Stopped")
    }
}
