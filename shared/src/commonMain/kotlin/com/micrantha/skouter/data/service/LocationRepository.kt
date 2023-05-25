package com.micrantha.skouter.data.service

import com.micrantha.skouter.domain.models.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


interface PresenceListener {
    fun onLocationChanged(latitude: Double, longitude: Double, accuracy: Float)
    fun onLocationEnabled() {}
    fun onLocationDisabled() {}
}

class LocationRepository : PresenceListener {

    private val current = MutableStateFlow(Location())

    fun location(): Flow<Location> = current

    override fun onLocationChanged(latitude: Double, longitude: Double, accuracy: Float) {
        current.update {
            it.copy(
                point = it.point.copy(latitude = latitude, longitude = longitude),
                accuracy = accuracy
            )
        }
    }
}
