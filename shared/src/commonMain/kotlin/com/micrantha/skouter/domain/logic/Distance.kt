package com.micrantha.skouter.domain.logic

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val earthRadius = 6371000.0 //meters

fun Double.toRadians() = this * (PI / 180)

fun distanceTo(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val dLat = (lat2 - lat1).toRadians()
    val dLng = (lon2 - lon1).toRadians()
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(lat1.toRadians()) * cos(lat2.toRadians()) * sin(dLng / 2) * sin(dLng / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return (earthRadius * c)
}
