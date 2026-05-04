package com.example.agri_sense.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    /** Returns the current district name via reverse geocoding, falls back to "Lilongwe" */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentDistrict(): String {
        return try {
            val cts = CancellationTokenSource()
            val location = fusedClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cts.token
            ).await()

            location?.let {
                val geocoder = Geocoder(context, Locale.ENGLISH)
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                // In Malawi, subAdminArea maps to district name
                addresses?.firstOrNull()?.subAdminArea
                    ?: addresses?.firstOrNull()?.adminArea
                    ?: "Lilongwe"
            } ?: "Lilongwe"
        } catch (e: Exception) {
            "Lilongwe" // Safe fallback
        }
    }

    /** Returns best-effort last known location for quick district detection */
    @SuppressLint("MissingPermission")
    suspend fun getLastKnownDistrict(): String {
        return try {
            val location = fusedClient.lastLocation.await()
            location?.let {
                val geocoder = Geocoder(context, Locale.ENGLISH)
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                addresses?.firstOrNull()?.subAdminArea ?: "Lilongwe"
            } ?: "Lilongwe"
        } catch (e: Exception) {
            "Lilongwe"
        }
    }

    fun isLocationEnabled(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
               lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
