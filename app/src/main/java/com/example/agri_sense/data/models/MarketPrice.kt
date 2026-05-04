package com.example.agri_sense.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "market_prices")
data class MarketPrice(
    @PrimaryKey val id: String,
    val cropName: String,
    val cropNameChichewa: String = "",
    val pricePerKg: Double,
    val priceUnit: String = "MWK",
    val marketName: String,
    val district: String,
    val region: String = "",
    val marketLocationLat: Double,
    val marketLocationLng: Double,
    val trendPercent: Double = 0.0,        // e.g. +8.5 means +8.5%
    val lastUpdated: Long = System.currentTimeMillis()
)
