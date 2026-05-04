package com.example.agri_sense.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agri_sense.data.models.WeatherAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherAlertDao {
    @Query("SELECT * FROM weather_alerts WHERE district = :district ORDER BY fetchedAt DESC LIMIT 1")
    fun getLatestForDistrict(district: String): Flow<WeatherAlert?>

    @Query("SELECT * FROM weather_alerts ORDER BY fetchedAt DESC LIMIT 1")
    fun getLatest(): Flow<WeatherAlert?>

    @Query("SELECT COUNT(*) FROM weather_alerts")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: WeatherAlert)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alerts: List<WeatherAlert>)

    @Query("DELETE FROM weather_alerts WHERE fetchedAt < :cutoff")
    suspend fun deleteStale(cutoff: Long)
}
