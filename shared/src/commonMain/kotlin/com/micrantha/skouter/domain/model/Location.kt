package com.micrantha.skouter.domain.model

import com.micrantha.skouter.domain.logic.distanceTo


data class Location(
    val point: Point = Point(),
    val data: Data? = null,
) : Comparable<Location> {

    data class Point(
        val latitude: Double = Double.NaN,
        val longitude: Double = Double.NaN
    ) : Comparable<Point> {

        val isValid = !latitude.isNaN() && !longitude.isNaN()

        override fun toString(): String {
            return "($latitude,$longitude)"
        }

        override fun compareTo(other: Point): Int {
            return distanceTo(latitude, longitude, other.latitude, other.longitude).toInt()
        }
    }

    data class Data(
        val name: String? = null,
        val city: String? = null,
        var region: String? = null,
        var country: String? = null,
        var accuracy: Float = Float.NaN
    ) : Comparable<Data> {
        override fun compareTo(other: Data) =
            accuracy.compareTo(other.accuracy)
    }

    override fun compareTo(other: Location): Int {
        return point.compareTo(other.point)
    }
}
