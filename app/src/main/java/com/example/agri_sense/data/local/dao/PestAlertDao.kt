package com.example.agri_sense.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.agri_sense.data.models.PestAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface PestAlertDao {
    @Query("SELECT * FROM pest_alerts ORDER BY reportedAt DESC")
    fun getAllAlerts(): Flow<List<PestAlert>>

    @Query("SELECT * FROM pest_alerts WHERE isRead = 0 ORDER BY reportedAt DESC")
    fun getUnreadAlerts(): Flow<List<PestAlert>>

    @Query("SELECT COUNT(*) FROM pest_alerts WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM pest_alerts")
    suspend fun getCount(): Int

    @Update
    suspend fun update(alert: PestAlert)

    @Query("UPDATE pest_alerts SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE pest_alerts SET isRead = 1")
    suspend fun markAllRead()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: PestAlert)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alerts: List<PestAlert>)
}
