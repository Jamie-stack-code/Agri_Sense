package com.example.agri_sense.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.agri_sense.data.local.AgriSenseDataStore
import com.example.agri_sense.data.repository.MarketRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class MarketSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val marketRepository: MarketRepository,
    private val dataStore: AgriSenseDataStore,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val isOffline = dataStore.offlineMode.first()
            if (isOffline) return Result.success()

            // Refresh live prices from backend
            marketRepository.syncLivePrices()

            // Check for user-defined price alerts
            val notifyMarket = dataStore.notifyMarket.first()
            if (notifyMarket) {
                val farmerId = dataStore.userId.first()
                if (farmerId != null) {
                    val alerts = marketRepository.getFarmerAlerts(farmerId)
                    val livePrices = marketRepository.allPrices.first()
                    
                    alerts.filter { it.isActive }.forEach { alert ->
                        val currentPrice = livePrices.find { it.cropName.equals(alert.cropName, ignoreCase = true) }?.pricePerKg ?: 0.0
                        
                        val isTriggered = if (alert.condition == "GREATER_THAN") {
                            currentPrice >= alert.targetPrice
                        } else {
                            currentPrice <= alert.targetPrice
                        }

                        if (isTriggered && currentPrice > 0) {
                            notificationHelper.sendMarketPriceAlert(alert.cropName, currentPrice)
                        }
                    }
                }

                // Also notify general spikes as fallback
                val allPrices = marketRepository.allPrices.first()
                val spiked = allPrices.filter { it.trendPercent >= 10.0 }
                spiked.firstOrNull()?.let { price ->
                    notificationHelper.sendMarketPriceAlert(price.cropName, price.pricePerKg)
                }
            }

            dataStore.setLastSyncTimestamp(System.currentTimeMillis().toString())
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
