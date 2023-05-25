package com.micrantha.skouter.data.service

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts

class PermissionManager(
    activity: ActivityResultCaller,
    private val presenceListener: PresenceListener
) {

    private val locationPermissionRequest = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                presenceListener.onLocationEnabled()
            }
            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                presenceListener.onLocationEnabled()
            }
            else -> {
                presenceListener.onLocationDisabled()
            }
        }
    }

    fun requestLocationPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}
