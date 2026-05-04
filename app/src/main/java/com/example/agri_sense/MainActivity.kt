package com.example.agri_sense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.agri_sense.services.MarketSyncWorker
import com.example.agri_sense.services.NotificationHelper
import com.example.agri_sense.services.WeatherSyncWorker
import com.example.agri_sense.ui.navigation.AppNavigation
import com.example.agri_sense.ui.theme.Agri_SenseTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create notification channels
        notificationHelper.createChannels()

        // Schedule background sync workers
        scheduleBackgroundSync()

        enableEdgeToEdge()
        setContent {
            Agri_SenseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation()
                    }
                }
            }
        }
    }

    private fun scheduleBackgroundSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Weather + Pest sync every 6 hours
        val weatherWork = PeriodicWorkRequestBuilder<WeatherSyncWorker>(6, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        // Market price sync every 4 hours
        val marketWork = PeriodicWorkRequestBuilder<MarketSyncWorker>(4, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).apply {
            enqueueUniquePeriodicWork(
                "weather_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                weatherWork
            )
            enqueueUniquePeriodicWork(
                "market_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                marketWork
            )
        }
    }
}
