package com.example.priority.view.task

import com.example.priority.Haversine
import org.junit.Assert.assertEquals
import org.junit.Test

class HaversineTest {

    @Test
    fun testHaversineDistance() {
        val lat1 = -6.923601 //Alun-alun Bandung
        val lon1 = 107.6072391
        val lat2 = -6.1892482 // Bandara Internasional Soekarno–Hatta
        val lon2 = 106.6620008

        val expectedDistanceKm = 133.097394 // Perkiraan jarak dalam km
        val actualDistance = Haversine.calculate(lat1, lon1, lat2, lon2)

        assertEquals(expectedDistanceKm, actualDistance, 1.0) // Allow small margin of error
    }
}
