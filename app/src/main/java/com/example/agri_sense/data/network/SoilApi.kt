package com.example.agri_sense.data.network

import com.example.agri_sense.data.models.SoilAnalysis
import retrofit2.Response
import retrofit2.http.*

interface SoilApi {
    @POST("api/soil-analysis")
    suspend fun submitAnalysis(@Body analysis: SoilAnalysisRequest): Response<SoilAnalysisResponse>

    @GET("api/soil-analysis/history/{farmerId}")
    suspend fun getHistory(@Path("farmerId") farmerId: String): Response<List<SoilAnalysisResponse>>
}

data class SoilAnalysisResponse(
    val id: String,
    val farmerId: String,
    val soilColor: String?,
    val soilType: String?,
    val nitrogen: String?,
    val phosphorus: String?,
    val potassium: String?,
    val pH: Double?,
    val recommendation: String?,
    val status: String,
    val expertComment: String?,
    val imageUrl: String?,
    val createdAt: String
)

data class SoilAnalysisRequest(
    val farmerId: String,
    val soilColor: String,
    val soilType: String,
    val nitrogen: String,
    val phosphorus: String,
    val potassium: String,
    val pH: Double,
    val recommendation: String,
    val imageUrl: String
)
