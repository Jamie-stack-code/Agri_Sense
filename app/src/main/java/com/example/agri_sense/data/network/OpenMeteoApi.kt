package com.example.agri_sense.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,wind_speed_10m",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,uv_index_max",
        @Query("timezone") timezone: String = "Africa/Maputo"
    ): WeatherResponse
}

data class WeatherResponse(
    val current: CurrentWeather,
    val daily: DailyWeather
)

data class CurrentWeather(
    val temperature_2m: Double,
    val relative_humidity_2m: Int,
    val wind_speed_10m: Double
)

data class DailyWeather(
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val uv_index_max: List<Double>
)
