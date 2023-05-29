package com.micrantha.skouter.domain.model

import com.micrantha.skouter.domain.logic.distanceTo


data class Location(
    val point: Point = Point(),
    val name: String? = null,
    val city: String? = null,
    var region: String? = null,
    var country: String? = null,
    var accuracy: Float = Float.NaN
) : Comparable<Location> {
    data class Point(
        val latitude: Double = Double.NaN,
        val longitude: Double = Double.NaN
    ) : Comparable<Point> {

        val isValid = !latitude.isNaN() && !longitude.isNaN()

        override fun compareTo(other: Point): Int {
            return distanceTo(latitude, longitude, other.latitude, other.longitude).toInt()
        }
    }

    override fun compareTo(other: Location): Int {
        return point.compareTo(other.point)
    }
}
