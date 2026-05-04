package com.example.agri_sense.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soil_analyses")
data class SoilAnalysis(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val farmerId: Int, // Foreign key implicitly or explicitly
    val timestamp: Long = System.currentTimeMillis(),
    val soilType: String,
    val soilQualityScore: Int,
    val wetPhotoUri: String? = null,
    val dryPhotoUri: String? = null,
    val generalRecommendation: String
)
