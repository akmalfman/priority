package com.example.priority

import java.text.DecimalFormat

//object EmissionCalculator {
//
//    fun calculate(distance: Double, vehicleType: String, fuelType: String): Double {
//        val emissionFactor = when {
//            vehicleType == "Motorcycle" && fuelType == "Bensin" -> 0.035
//            vehicleType == "Motorcycle" && fuelType == "Listrik" -> 0.021
//            vehicleType == "Car" && fuelType == "Listrik" -> 0.031
//            vehicleType == "Car" && fuelType == "Bensin" -> 0.088
//            vehicleType == "Car" && fuelType == "Solar" -> 0.136
//            vehicleType == "Bus" && fuelType == "Listrik" -> 0.006
//            vehicleType == "Bus" && fuelType == "Solar" -> 0.008
//            vehicleType == "Train" && fuelType == "Listrik" -> 0.028
//            else -> 0.0
//        }
//        return distance * emissionFactor
//    }
//
//    fun formatEmission(emission: Double): String {
//        val decimalFormat = DecimalFormat("#.###")
//        return decimalFormat.format(emission)
//    }
//}


object EmissionCalculator {

    // Enum untuk jenis kendaraan
    enum class VehicleType { Motorcycle, Car, Bus, Train }

    // Enum untuk jenis bahan bakar
    enum class FuelType { Bensin, Solar, Listrik }

    // Faktor emisi disimpan dalam Map untuk mempermudah pembaruan
    private val emissionFactors: Map<Pair<VehicleType, FuelType>, Double> = mapOf(
        VehicleType.Motorcycle to FuelType.Bensin to 0.035,
        VehicleType.Motorcycle to FuelType.Listrik to 0.021,
        VehicleType.Car to FuelType.Listrik to 0.031,
        VehicleType.Car to FuelType.Bensin to 0.088,
        VehicleType.Car to FuelType.Solar to 0.136,
        VehicleType.Bus to FuelType.Listrik to 0.006,
        VehicleType.Bus to FuelType.Solar to 0.008,
        VehicleType.Train to FuelType.Listrik to 0.028
    )

    fun calculate(distance: Double, vehicle: String, fuel: String): Double {
        val vehicleType = VehicleType.values().find { it.name.equals(vehicle, ignoreCase = true) }
        val fuelType = FuelType.values().find { it.name.equals(fuel, ignoreCase = true) }

        val emissionFactor = emissionFactors[vehicleType to fuelType] ?: 0.0
        return distance * emissionFactor
    }

    fun formatEmission(emission: Double): String {
        val decimalFormat = DecimalFormat("#.###")
        return decimalFormat.format(emission)
    }
}