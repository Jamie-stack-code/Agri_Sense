package com.example.agri_sense.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agri_sense.data.models.MarketPrice
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketPriceDao {
    @Query("SELECT * FROM market_prices ORDER BY lastUpdated DESC")
    fun getAllPrices(): Flow<List<MarketPrice>>

    @Query("SELECT * FROM market_prices WHERE district = :district ORDER BY trendPercent DESC")
    fun getPricesByDistrict(district: String): Flow<List<MarketPrice>>

    @Query("SELECT * FROM market_prices WHERE cropName LIKE '%' || :query || '%' ORDER BY lastUpdated DESC")
    fun searchPrices(query: String): Flow<List<MarketPrice>>

    @Query("SELECT COUNT(*) FROM market_prices")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(prices: List<MarketPrice>)

    @Query("DELETE FROM market_prices WHERE lastUpdated < :cutoff")
    suspend fun deleteStale(cutoff: Long)
}
