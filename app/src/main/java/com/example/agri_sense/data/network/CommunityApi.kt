package com.example.agri_sense.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommunityApi {
    @GET("api/stats/farmer-count")
    suspend fun getFarmerCount(): FarmerCountResponse

    @POST("api/diagnostics")
    suspend fun submitDiagnosticReport(@Body request: DiagnosticReportRequest): DiagnosticReportResponse

    @GET("api/diagnostics/history/{farmerId}")
    suspend fun getDiagnosticHistory(@Path("farmerId") farmerId: String): List<DiagnosticReportResponse>
}
