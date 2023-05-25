package com.micrantha.skouter.data.service

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat

class LocationManager(
    private val context: Context,
    private val listener: PresenceListener,
    private val permissionManager: PermissionManager,
    private var minLocationTime: Long = 1000L,
    private var minLocationDistance: Float = 3.0f
) : LocationListener {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    companion object {
        const val TAG = "LocationManager"
    }

    fun start() {

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minLocationTime, minLocationDistance, this
            )

            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minLocationTime, minLocationDistance, this
            )

            locationManager.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER,
                minLocationTime, minLocationDistance, this
            )
        } else {
            permissionManager.requestLocationPermissions()
        }
    }


    private fun isStarted(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
    }

    fun stop() {
        locationManager.removeUpdates(this)
    }

    override fun onProviderEnabled(provider: String) {
        Log.d(TAG, "$provider enabled")
        when (provider) {
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER -> listener.onLocationEnabled()
        }
    }

    override fun onProviderDisabled(provider: String) {
        Log.d(TAG, "$provider disabled")
        if (!isStarted()) {
            listener.onLocationDisabled()
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d(
            TAG,
            "location: ${location.latitude}:${location.longitude} - ${location.accuracy}"
        )

        listener.onLocationChanged(location.latitude, location.longitude, location.accuracy)
    }

}
