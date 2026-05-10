package com.example.agri_sense.data.repository

import com.example.agri_sense.data.local.dao.FarmerDao
import com.example.agri_sense.data.models.Farmer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FarmerRepository @Inject constructor(private val farmerDao: FarmerDao) {

    val farmerFlow: Flow<Farmer?> = farmerDao.getFarmer()

    suspend fun getFarmerOnce(): Farmer? = farmerDao.getFarmerOnce()

    suspend fun saveFarmer(farmer: Farmer) = farmerDao.insertFarmer(farmer)

    suspend fun updateFarmer(farmer: Farmer) = farmerDao.updateFarmer(farmer)

    suspend fun clearAll() = farmerDao.clearAll()

    suspend fun isOnboarded(): Boolean = farmerDao.getFarmerOnce()?.isOnboarded == true

    suspend fun isPremium(): Boolean {
        val farmer = farmerDao.getFarmerOnce() ?: return false
        return farmer.subscriptionStatus == "PREMIUM" &&
               farmer.subscriptionExpiry > System.currentTimeMillis()
    }

    suspend fun activatePremium(farmer: Farmer, durationDays: Int = 365) {
        val expiryMs = System.currentTimeMillis() + (durationDays * 24L * 60 * 60 * 1000)
        farmerDao.updateFarmer(
            farmer.copy(
                subscriptionStatus = "PREMIUM",
                subscriptionExpiry = expiryMs
            )
        )
    }
}
