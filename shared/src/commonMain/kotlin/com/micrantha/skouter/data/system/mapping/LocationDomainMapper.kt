package com.micrantha.skouter.data.system.mapping

import com.micrantha.skouter.domain.model.Location.Point
import dev.icerock.moko.geo.LatLng

fun Point.copy(latLng: LatLng) = this.copy(
    latitude = latLng.latitude,
    longitude = latLng.longitude
)
