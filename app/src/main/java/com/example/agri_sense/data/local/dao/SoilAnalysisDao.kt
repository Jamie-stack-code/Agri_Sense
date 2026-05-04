package com.example.agri_sense.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agri_sense.data.models.SoilAnalysis
import kotlinx.coroutines.flow.Flow

@Dao
interface SoilAnalysisDao {
    @Query("SELECT * FROM soil_analyses ORDER BY timestamp DESC")
    fun getAllAnalyses(): Flow<List<SoilAnalysis>>

    @Query("SELECT * FROM soil_analyses WHERE farmerId = :farmerId ORDER BY timestamp DESC")
    fun getAnalysesForFarmer(farmerId: Int): Flow<List<SoilAnalysis>>

    @Query("SELECT * FROM soil_analyses ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestAnalysis(): SoilAnalysis?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysis(analysis: SoilAnalysis)
}
