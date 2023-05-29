package com.micrantha.skouter.data.system

import com.micrantha.skouter.data.system.mapping.copy
import com.micrantha.skouter.data.system.source.LocationLocalSource
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.updateAndGet

class LocationDataRepository(
    private val localSource: LocationLocalSource,
    //private val geocodeSource: GeoCodeSource,
) : LocationRepository {
    private val location = MutableStateFlow<Location?>(null)

    override fun currentLocation() = location.value

    override fun locations(): Flow<Location> =
        localSource.getLocationsFlow().map { point ->
            location.updateAndGet { loc ->
                loc?.copy(point = loc.point.copy(latLng = point))
            }
        }.filterNotNull()
}
