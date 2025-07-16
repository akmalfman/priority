package com.example.priority

import kotlin.math.*

//object Haversine {
//    fun calculate(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val R = 6371 // Radius bumi dalam meter
//        val dLat = Math.toRadians(lat2 - lat1)
//        val dLon = Math.toRadians(lon2 - lon1)
//        val a = sin(dLat / 2) * sin(dLat / 2) +
//                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
//                sin(dLon / 2) * sin(dLon / 2)
//        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
//        return R * c
//    }
//}

object Haversine {
    private const val RADIUS_EARTH = 6371

    fun calculate(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = (lat2 - lat1).toRadians()
        val dLon = (lon2 - lon1).toRadians()
        val a = sin(dLat / 2).pow(2) +
                cos(lat1.toRadians()) * cos(lat2.toRadians()) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return RADIUS_EARTH * c
    }

    private fun Double.toRadians() = this * (PI / 180)
}