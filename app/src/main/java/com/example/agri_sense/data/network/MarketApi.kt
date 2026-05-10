package com.example.agri_sense.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MarketApi {
    @GET("market/live")
    suspend fun getLivePrices(): Response<List<MarketPriceResponse>>

    @POST("market/alerts")
    suspend fun setPriceAlert(@Body request: PriceAlertRequest): Response<PriceAlertResponse>

    @GET("market/alerts/{farmerId}")
    suspend fun getFarmerAlerts(@Path("farmerId") farmerId: String): Response<List<PriceAlertResponse>>
}

data class MarketPriceResponse(
    val id: String,
    val cropName: String,
    val cropNameChichewa: String?,
    val pricePerKg: Double,
    val marketName: String,
    val district: String,
    val trend: Double,
    val lastUpdated: String
)

data class PriceAlertRequest(
    val farmerId: String,
    val cropName: String,
    val targetPrice: Double,
    val condition: String = "GREATER_THAN"
)

data class PriceAlertResponse(
    val id: String,
    val cropName: String,
    val targetPrice: Double,
    val condition: String,
    val isActive: Boolean
)
