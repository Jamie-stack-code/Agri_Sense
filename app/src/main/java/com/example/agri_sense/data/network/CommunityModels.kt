package com.example.agri_sense.data.network

data class FarmerCountResponse(val count: Int)

data class DiagnosticReportRequest(
    val imageUrl: String,
    val cropType: String,
    val aiDiagnosis: String,
    val farmerId: String
)

data class DiagnosticReportResponse(
    val id: String,
    val status: String,
    val cropType: String?,
    val aiDiagnosis: String?,
    val expertRecommendation: String?,
    val createdAt: String
)
