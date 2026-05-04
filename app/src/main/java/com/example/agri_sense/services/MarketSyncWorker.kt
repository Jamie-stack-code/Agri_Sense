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

            // Seed/refresh market data
            marketRepository.seedIfEmpty()

            // Check for price spikes and notify
            val notifyMarket = dataStore.notifyMarket.first()
            if (notifyMarket) {
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
