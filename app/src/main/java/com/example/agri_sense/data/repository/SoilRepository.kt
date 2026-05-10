package com.example.agri_sense.data.repository

import android.net.Uri
import com.example.agri_sense.data.local.dao.SoilAnalysisDao
import com.example.agri_sense.data.models.SoilAnalysis
import com.example.agri_sense.data.network.SoilAnalysisRequest
import com.example.agri_sense.data.network.SoilAnalysisResponse
import com.example.agri_sense.data.network.SoilApi
import com.example.agri_sense.ml.SoilClassifier
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoilRepository @Inject constructor(
    private val soilAnalysisDao: SoilAnalysisDao,
    private val soilClassifier: SoilClassifier,
    private val soilApi: SoilApi,
    private val aiExpertRepository: AiExpertRepository
) {
    fun getAllAnalyses(): Flow<List<SoilAnalysis>> = soilAnalysisDao.getAllAnalyses()

    fun getAnalysesForFarmer(farmerId: Int): Flow<List<SoilAnalysis>> =
        soilAnalysisDao.getAnalysesForFarmer(farmerId)

    suspend fun syncHistory(farmerId: String) {
        try {
            val response = soilApi.getHistory(farmerId)
            if (response.isSuccessful) {
                val remoteAnalyses = response.body() ?: emptyList()
                // Update local DB with remote data
                remoteAnalyses.forEach { remote ->
                    val local = SoilAnalysis(
                        serverId = remote.id.toString(),
                        farmerId = farmerId.toInt(),
                        soilType = remote.soilType ?: "Unknown",
                        soilColor = remote.soilColor,
                        nitrogen = remote.nitrogen,
                        phosphorus = remote.phosphorus,
                        potassium = remote.potassium,
                        pH = remote.pH,
                        soilQualityScore = 75, // Placeholder
                        generalRecommendation = remote.recommendation ?: "",
                        expertComment = remote.expertComment,
                        status = remote.status
                    )
                    // You might want to upsert here
                    soilAnalysisDao.insertAnalysis(local)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun runAndSaveAnalysis(farmerId: Int, dryUri: Uri, wetUri: Uri): SoilClassifier.SoilResult {
        val result = soilClassifier.analyzeSoil(dryUri, wetUri)
        val qualityScore = soilClassifier.calculateQualityScore(result)
        
        // Fetch Real AI Recommendation
        val recommendation = aiExpertRepository.getSoilRecommendation(
            soilType = result.type,
            pH = result.properties.pH,
            nitrogen = result.properties.nitrogen,
            phosphorus = result.properties.phosphorus,
            potassium = result.properties.potassium,
            isEnglish = true // default to english for logic, can be improved
        )
        
        val localAnalysis = SoilAnalysis(
            farmerId = farmerId,
            soilType = result.type,
            nitrogen = result.properties.nitrogen,
            phosphorus = result.properties.phosphorus,
            potassium = result.properties.potassium,
            pH = result.properties.pH.toDouble(),
            soilQualityScore = qualityScore,
            dryPhotoUri = dryUri.toString(),
            wetPhotoUri = wetUri.toString(),
            generalRecommendation = recommendation,
            status = "EXPERT_PENDING"
        )
        
        val id = soilAnalysisDao.insertAnalysis(localAnalysis)
        
        // Sync to Backend
        try {
            val request = SoilAnalysisRequest(
                farmerId = farmerId.toString(),
                soilColor = "Brown", 
                soilType = result.type,
                nitrogen = result.properties.nitrogen,
                phosphorus = result.properties.phosphorus,
                potassium = result.properties.potassium,
                pH = result.properties.pH.toDouble(),
                recommendation = recommendation,
                imageUrl = dryUri.toString() 
            )
            val response = soilApi.submitAnalysis(request)
            if (response.isSuccessful) {
                val remote = response.body()
                if (remote != null) {
                    // Update local with server ID
                    soilAnalysisDao.insertAnalysis(localAnalysis.copy(id = id.toInt(), serverId = remote.id))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return result
    }
}
