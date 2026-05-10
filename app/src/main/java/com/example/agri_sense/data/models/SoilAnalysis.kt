package com.example.agri_sense.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soil_analyses")
data class SoilAnalysis(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serverId: String? = null,
    val farmerId: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val soilType: String,
    val soilColor: String? = null,
    val nitrogen: String? = null,
    val phosphorus: String? = null,
    val potassium: String? = null,
    val pH: Double? = null,
    val soilQualityScore: Int,
    val wetPhotoUri: String? = null,
    val dryPhotoUri: String? = null,
    val generalRecommendation: String,
    val expertComment: String? = null,
    val status: String = "AI_COMPLETED"
)
