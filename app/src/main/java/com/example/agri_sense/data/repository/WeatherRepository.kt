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
            "Balaka" -> Pair(-14.99, 34.96)
            "Blantyre" -> Pair(-15.78, 35.00)
            "Chikwawa" -> Pair(-16.03, 34.80)
            "Chiradzulu" -> Pair(-15.68, 35.14)
            "Chitipa" -> Pair(-9.71, 33.27)
            "Dedza" -> Pair(-14.38, 34.33)
            "Dowa" -> Pair(-13.65, 33.94)
            "Karonga" -> Pair(-9.93, 33.93)
            "Kasungu" -> Pair(-13.03, 33.48)
            "Lilongwe" -> Pair(-13.98, 33.78)
            "Machinga" -> Pair(-14.82, 35.53)
            "Mangochi" -> Pair(-14.48, 35.26)
            "Mchinji" -> Pair(-13.80, 32.88)
            "Mulanje" -> Pair(-16.03, 35.51)
            "Mwanza" -> Pair(-15.61, 34.51)
            "Mzimba" -> Pair(-11.90, 33.60)
            "Neno" -> Pair(-15.40, 34.65)
            "Nkhata Bay" -> Pair(-11.61, 34.30)
            "Nkhotakota" -> Pair(-12.93, 34.30)
            "Nsanje" -> Pair(-16.92, 35.26)
            "Ntcheu" -> Pair(-14.82, 34.64)
            "Ntchisi" -> Pair(-13.33, 34.01)
            "Phalombe" -> Pair(-15.81, 35.65)
            "Rumphi" -> Pair(-11.02, 33.86)
            "Salima" -> Pair(-13.78, 34.43)
            "Thyolo" -> Pair(-16.07, 35.14)
            "Zomba" -> Pair(-15.38, 35.32)
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
                condition = mapWeatherCode(current.weather_code, true),
                conditionChichewa = mapWeatherCode(current.weather_code, false),
                temperatureC = current.temperature_2m,
                temperatureHigh = daily.temperature_2m_max.firstOrNull() ?: current.temperature_2m,
                temperatureLow = daily.temperature_2m_min.firstOrNull() ?: current.temperature_2m,
                humidity = current.relative_humidity_2m,
                rainfallMm = daily.precipitation_sum.firstOrNull() ?: current.precipitation,
                windSpeedKmh = current.wind_speed_10m,
                uvIndex = daily.uv_index_max.firstOrNull()?.toInt() ?: 6,
                severeWarning = (daily.precipitation_sum.firstOrNull() ?: 0.0) > 50.0,
                warningMessage = if ((daily.precipitation_sum.firstOrNull() ?: 0.0) > 50.0) "Heavy Flood Risk" else "",
                warningMessageChichewa = if ((daily.precipitation_sum.firstOrNull() ?: 0.0) > 50.0) "Chiwopsezo cha Chigumula" else "",
                validFrom = now,
                validTo = now + dayMs,
                fetchedAt = now
            )
            weatherAlertDao.insert(alert)
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback mock data
            val now = System.currentTimeMillis()
            val dayMs = 24 * 60 * 60 * 1000L
            weatherAlertDao.insert(WeatherAlert(
                id = "w_fallback_${System.currentTimeMillis()}", district = district, 
                condition = "Partly Cloudy", conditionChichewa = "Mitambo Yochepa",
                temperatureC = 24.0, temperatureHigh = 29.0, temperatureLow = 18.0, humidity = 62, 
                rainfallMm = 2.0, windSpeedKmh = 14.0, uvIndex = 7, severeWarning = false,
                warningMessage = "", warningMessageChichewa = "", validFrom = now, validTo = now + dayMs, fetchedAt = now
            ))
        }
    }

    private fun mapWeatherCode(code: Int, isEnglish: Boolean): String {
        return if (isEnglish) {
            when (code) {
                0 -> "Clear Sky"
                1, 2, 3 -> "Partly Cloudy"
                45, 48 -> "Foggy"
                51, 53, 55 -> "Drizzle"
                61, 63, 65 -> "Rainy"
                71, 73, 75 -> "Snowy"
                80, 81, 82 -> "Rain Showers"
                95, 96, 99 -> "Thunderstorm"
                else -> "Cloudy"
            }
        } else {
            when (code) {
                0 -> "Dzuwa"
                1, 2, 3 -> "Mitambo"
                45, 48 -> "Khungu"
                51, 53, 55 -> "Mvula Yochepa"
                61, 63, 65 -> "Mvula"
                71, 73, 75 -> "Chipale Chofewa"
                80, 81, 82 -> "Mvula Zamphamvu"
                95, 96, 99 -> "Mvula Yamabingu"
                else -> "Mitambo"
            }
        }
    }
}
