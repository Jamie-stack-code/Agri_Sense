package com.example.agri_sense.data.repository

import android.net.Uri
import com.example.agri_sense.data.local.dao.SoilAnalysisDao
import com.example.agri_sense.data.models.SoilAnalysis
import com.example.agri_sense.ml.SoilClassifier
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoilRepository @Inject constructor(
    private val soilAnalysisDao: SoilAnalysisDao,
    private val soilClassifier: SoilClassifier
) {
    fun getAllAnalyses(): Flow<List<SoilAnalysis>> = soilAnalysisDao.getAllAnalyses()

    fun getAnalysesForFarmer(farmerId: Int): Flow<List<SoilAnalysis>> =
        soilAnalysisDao.getAnalysesForFarmer(farmerId)

    suspend fun runAndSaveAnalysis(farmerId: Int, dryUri: Uri, wetUri: Uri): SoilClassifier.SoilResult {
        val result = soilClassifier.analyzeSoil(dryUri, wetUri)
        val qualityScore = soilClassifier.calculateQualityScore(result)
        val recommendation = "Soil type: ${result.type}. pH: ${result.properties.pH}. " +
            "N: ${result.properties.nitrogen}, P: ${result.properties.phosphorus}, K: ${result.properties.potassium}."
        soilAnalysisDao.insertAnalysis(
            SoilAnalysis(
                farmerId = farmerId,
                soilType = result.type,
                soilQualityScore = qualityScore,
                dryPhotoUri = dryUri.toString(),
                wetPhotoUri = wetUri.toString(),
                generalRecommendation = recommendation
            )
        )
        return result
    }
}
