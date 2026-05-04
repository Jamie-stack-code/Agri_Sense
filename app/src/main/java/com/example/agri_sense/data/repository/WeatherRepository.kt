package com.example.agri_sense.data.repository

import com.example.agri_sense.data.local.dao.WeatherAlertDao
import com.example.agri_sense.data.models.WeatherAlert
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val weatherAlertDao: WeatherAlertDao) {

    fun getWeatherForDistrict(district: String): Flow<WeatherAlert?> =
        weatherAlertDao.getLatestForDistrict(district)

    val latestWeather: Flow<WeatherAlert?> = weatherAlertDao.getLatest()

    suspend fun seedIfEmpty() {
        if (weatherAlertDao.getCount() > 0) return
        val now = System.currentTimeMillis()
        val dayMs = 24 * 60 * 60 * 1000L
        weatherAlertDao.insertAll(listOf(
            WeatherAlert("w1", "Lilongwe", "Partly Cloudy", "Mitambo Yochepa",
                24.0, 29.0, 18.0, 62, 2.0, 14.0, 7, false,
                "", "", now, now + dayMs, now),
            WeatherAlert("w2", "Blantyre", "Hot and Humid", "Kutentha ndi Chinyezi",
                28.0, 34.0, 22.0, 75, 0.0, 8.0, 9, false,
                "", "", now, now + dayMs, now),
            WeatherAlert("w3", "Mzuzu", "Light Rain", "Mvula Yochepa",
                19.0, 23.0, 15.0, 82, 8.5, 20.0, 4, false,
                "", "", now, now + dayMs, now),
            WeatherAlert("w4", "Kasungu", "Thunderstorm Warning", "Chenjezero cha Mphenzi",
                21.0, 26.0, 17.0, 90, 35.0, 45.0, 3, true,
                "Severe thunderstorms expected. Do not work in open fields between 14:00-18:00.",
                "Mphenzi yolimbikira ikudikirika. Musagwire ntchito m'minda kuchokera 14:00 mpaka 18:00.",
                now, now + dayMs, now),
            WeatherAlert("w5", "Zomba", "Clear and Sunny", "Dzuwa Lokongola",
                26.0, 31.0, 20.0, 55, 0.0, 10.0, 10, false,
                "", "", now, now + dayMs, now),
            WeatherAlert("w6", "Salima", "Humid, Chance of Rain", "Chinyezi, Mvula Yangapo",
                25.0, 30.0, 21.0, 80, 12.0, 18.0, 6, false,
                "", "", now, now + dayMs, now),
            WeatherAlert("w7", "Karonga", "Hot and Dry", "Kutentha ndi Chilala",
                31.0, 38.0, 25.0, 40, 0.0, 22.0, 11, true,
                "Drought risk: High temperatures and no rainfall forecast for 10 days. Water crops urgently.",
                "Chilala chachikulu: Kutentha kwakukulu ndipo mvula siyiyembekezeka masiku 10. Podzerani madzi mosamalitsa.",
                now, now + (10 * dayMs), now)
        ))
    }
}
