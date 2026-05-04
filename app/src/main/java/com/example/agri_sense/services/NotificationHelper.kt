package com.example.agri_sense.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_PEST_ALERTS  = "channel_pest_alerts"
        const val CHANNEL_MARKET_PRICE = "channel_market_price"
        const val CHANNEL_WEATHER      = "channel_weather"

        const val NOTIF_ID_PEST   = 1001
        const val NOTIF_ID_MARKET = 1002
        const val NOTIF_ID_WEATHER = 1003
    }

    fun createChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_PEST_ALERTS,
                "Pest Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical pest outbreak alerts for your region"
                enableVibration(true)
            }
        )
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_MARKET_PRICE,
                "Market Prices",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Market price updates and alerts"
            }
        )
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_WEATHER,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Severe weather warnings for your district"
                enableVibration(true)
            }
        )
    }

    fun sendPestAlert(pestName: String, district: String, severity: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_PEST_ALERTS)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("⚠️ $severity Pest Alert: $pestName")
            .setContentText("Outbreak reported in $district. Tap to see recommendations.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        try {
            NotificationManagerCompat.from(context).notify(NOTIF_ID_PEST, notification)
        } catch (_: SecurityException) { /* POST_NOTIFICATIONS permission not granted */ }
    }

    fun sendWeatherWarning(district: String, message: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_WEATHER)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🌩 Weather Warning — $district")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        try {
            NotificationManagerCompat.from(context).notify(NOTIF_ID_WEATHER, notification)
        } catch (_: SecurityException) { }
    }

    fun sendMarketPriceAlert(cropName: String, price: Double) {
        val notification = NotificationCompat.Builder(context, CHANNEL_MARKET_PRICE)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("📈 Price Alert: $cropName")
            .setContentText("$cropName is now at MK ${price.toInt()}/kg. Good time to sell!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        try {
            NotificationManagerCompat.from(context).notify(NOTIF_ID_MARKET, notification)
        } catch (_: SecurityException) { }
    }
}
