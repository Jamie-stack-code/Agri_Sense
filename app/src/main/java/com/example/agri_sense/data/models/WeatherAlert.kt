package com.example.agri_sense.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_alerts")
data class WeatherAlert(
    @PrimaryKey val id: String,
    val district: String,
    val condition: String,                 // "Partly Cloudy", "Heavy Rain", "Drought Risk"
    val conditionChichewa: String = "",
    val temperatureC: Double,
    val temperatureHigh: Double = 0.0,
    val temperatureLow: Double = 0.0,
    val humidity: Int,
    val rainfallMm: Double = 0.0,
    val windSpeedKmh: Double = 0.0,
    val uvIndex: Int = 3,
    val severeWarning: Boolean = false,
    val warningMessage: String = "",
    val warningMessageChichewa: String = "",
    val validFrom: Long = System.currentTimeMillis(),
    val validTo: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000L),
    val fetchedAt: Long = System.currentTimeMillis()
)
