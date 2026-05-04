package com.example.agri_sense.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pest_alerts")
data class PestAlert(
    @PrimaryKey val id: String,
    val pestName: String,
    val pestNameChichewa: String = "",
    val affectedCrops: String,             // Comma-separated: "Maize,Sorghum"
    val outbreakDistricts: String,         // Comma-separated: "Lilongwe,Kasungu"
    val severityLevel: String,             // "LOW" | "MEDIUM" | "HIGH" | "CRITICAL"
    val description: String,
    val descriptionChichewa: String = "",
    val recommendedAction: String,
    val recommendedActionChichewa: String = "",
    val imageRes: String = "",             // drawable resource name
    val reportedAt: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
