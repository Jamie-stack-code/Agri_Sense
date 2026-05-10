package com.example.agri_sense.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmers")
data class Farmer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String = "",
    val district: String,
    val region: String = "",               // "Northern" | "Central" | "Southern"
    val farmSize: Double,
    val farmSizeUnit: String = "Ha",
    val cropsGrown: String,                // Comma-separated: "Maize,Groundnuts"
    val preferredLanguage: String = "English",
    val subscriptionStatus: String = "FREE",   // "FREE" or "PREMIUM"
    val subscriptionExpiry: Long = 0L,
    val avatarUri: String = "",
    val isOnboarded: Boolean = false,
    val isProfileComplete: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
