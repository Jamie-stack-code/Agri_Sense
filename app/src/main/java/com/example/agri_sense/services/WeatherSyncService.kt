package com.example.agri_sense.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.agri_sense.data.local.AgriSenseDataStore
import com.example.agri_sense.data.repository.FarmerRepository
import com.example.agri_sense.data.repository.PestAlertRepository
import com.example.agri_sense.data.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val weatherRepository: WeatherRepository,
    private val pestRepository: PestAlertRepository,
    private val farmerRepository: FarmerRepository,
    private val dataStore: AgriSenseDataStore,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val isOffline = dataStore.offlineMode.first()
            if (isOffline) return Result.success()

            val district = farmerRepository.getFarmerOnce()?.district ?: "Lilongwe"

            // Refresh weather seed data
            weatherRepository.seedIfEmpty()

            // Check for severe weather and notify
            val notifyWeather = dataStore.notifyWeather.first()
            if (notifyWeather) {
                val weather = weatherRepository.getWeatherForDistrict(district).first()
                if (weather?.severeWarning == true && weather.warningMessage.isNotEmpty()) {
                    notificationHelper.sendWeatherWarning(district, weather.warningMessage)
                }
            }

            // Check for critical pest alerts and notify
            val notifyPest = dataStore.notifyPest.first()
            if (notifyPest) {
                val criticalPests = pestRepository.unreadAlerts.first()
                    .filter { it.severityLevel == "CRITICAL" || it.severityLevel == "HIGH" }
                    .filter { it.outbreakDistricts.contains(district, ignoreCase = true) }
                criticalPests.firstOrNull()?.let { alert ->
                    notificationHelper.sendPestAlert(alert.pestName, district, alert.severityLevel)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
