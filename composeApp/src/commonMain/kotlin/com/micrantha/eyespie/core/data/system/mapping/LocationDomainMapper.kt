package com.micrantha.eyespie.core.data.system.mapping

import com.micrantha.eyespie.domain.entities.Location
import com.micrantha.eyespie.domain.entities.Location.Point
import dev.icerock.moko.geo.LatLng
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive

class LocationDomainMapper {

    fun map(data: JsonElement): Location? {
        return point(data.jsonPrimitive.content)?.let { Location(point = it) }
    }

    fun point(data: String): Point? {
        var start = data.indexOf('(')
        if (start == -1) return null
        var end = data.indexOf(',', start)
        if (end == -1 || start >= end) return null
        val latitude = data.substring(start + 1, end).toDouble()

        start = end
        end = data.indexOf(')', start)
        if (end == -1 || start >= end) return null
        val longitude = data.substring(start + 1, end).toDouble()

        return Point(latitude = latitude, longitude = longitude)
    }
}

fun Point.copy(latLng: LatLng) = this.copy(
    latitude = latLng.latitude,
    longitude = latLng.longitude
)
