package com.example.agri_sense.data.repository

import com.example.agri_sense.data.local.dao.WeatherAlertDao
import com.example.agri_sense.data.models.WeatherAlert
import com.example.agri_sense.data.network.OpenMeteoApi
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherAlertDao: WeatherAlertDao,
    private val api: OpenMeteoApi
) {

    fun getWeatherForDistrict(district: String): Flow<WeatherAlert?> =
        weatherAlertDao.getLatestForDistrict(district)

    val latestWeather: Flow<WeatherAlert?> = weatherAlertDao.getLatest()

    suspend fun seedIfEmpty() {
        if (weatherAlertDao.getCount() > 0) return
        syncWeatherForDistrict("Lilongwe")
    }

    suspend fun syncWeatherForDistrict(district: String) {
        val coords = when (district) {
            "Blantyre" -> Pair(-15.78, 35.00)
            "Mzuzu" -> Pair(-11.46, 34.02)
            else -> Pair(-13.98, 33.78) // Lilongwe default
        }

        try {
            val response = api.getWeather(coords.first, coords.second)
            val current = response.current
            val daily = response.daily

            val now = System.currentTimeMillis()
            val dayMs = 24 * 60 * 60 * 1000L

            val alert = WeatherAlert(
                id = UUID.randomUUID().toString(),
                district = district,
                condition = "Clear", // Heuristic could be added based on other params
                conditionChichewa = "Dzuwa Lokongola",
                temperatureC = current.temperature_2m,
                temperatureHigh = daily.temperature_2m_max.firstOrNull() ?: current.temperature_2m,
                temperatureLow = daily.temperature_2m_min.firstOrNull() ?: current.temperature_2m,
                humidity = current.relative_humidity_2m,
                rainfallMm = 0.0,
                windSpeedKmh = current.wind_speed_10m,
                uvIndex = daily.uv_index_max.firstOrNull()?.toInt() ?: 6,
                severeWarning = false,
                warningMessage = "",
                warningMessageChichewa = "",
                validFrom = now,
                validTo = now + dayMs,
                fetchedAt = now
            )
            weatherAlertDao.insert(alert)
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to mock data if API fails
            val now = System.currentTimeMillis()
            val dayMs = 24 * 60 * 60 * 1000L
            weatherAlertDao.insert(WeatherAlert(
                id = "w1", district = "Lilongwe", condition = "Partly Cloudy", conditionChichewa = "Mitambo Yochepa",
                temperatureC = 24.0, temperatureHigh = 29.0, temperatureLow = 18.0, humidity = 62, 
                rainfallMm = 2.0, windSpeedKmh = 14.0, uvIndex = 7, severeWarning = false,
                warningMessage = "", warningMessageChichewa = "", validFrom = now, validTo = now + dayMs, fetchedAt = now
            ))
        }
    }
}
