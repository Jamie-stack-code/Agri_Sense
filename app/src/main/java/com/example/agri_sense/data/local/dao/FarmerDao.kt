package com.example.agri_sense.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.agri_sense.data.models.Farmer
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmerDao {
    @Query("SELECT * FROM farmers LIMIT 1")
    fun getFarmer(): Flow<Farmer?>

    @Query("SELECT * FROM farmers LIMIT 1")
    suspend fun getFarmerOnce(): Farmer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarmer(farmer: Farmer)

    @Update
    suspend fun updateFarmer(farmer: Farmer)

    @Query("DELETE FROM farmers")
    suspend fun clearAll()
}
