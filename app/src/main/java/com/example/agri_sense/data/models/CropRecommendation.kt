package com.example.agri_sense.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crop_recommendations")
data class CropRecommendation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val soilAnalysisId: Int,
    val cropName: String,
    val cropNameChichewa: String = "",
    val confidenceScore: Float,           // 0.0 – 1.0
    val reasonSummary: String,
    val plantingGuide: String,
    val fertilizerAdvice: String,
    val wateringNeeds: String,
    val expectedYieldKgPerHa: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
