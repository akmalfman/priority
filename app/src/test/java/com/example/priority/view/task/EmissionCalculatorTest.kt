package com.example.priority.view.task

import com.example.priority.EmissionCalculator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DecimalFormat

class EmissionCalculatorTest {

    private val decimalFormat = DecimalFormat("#.###")

    @Test
    fun testCalculateEmissions_MotorcycleBensin() {
        val distance = 10.0
        val result = EmissionCalculator.calculate(distance, "Motorcycle", "Bensin")
        val expected = 10.0 * 0.035
        assertEquals(decimalFormat.format(expected), decimalFormat.format(result))
    }

    @Test
    fun testFormatEmission() {
        val emission = 12.34567
        val formattedEmission = EmissionCalculator.formatEmission(emission)
        assertEquals("12.346", formattedEmission)  // Karena DecimalFormat("#.###")
    }
}
