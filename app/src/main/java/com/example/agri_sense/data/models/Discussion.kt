package com.example.agri_sense.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discussions")
data class Discussion(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorDistrict: String,
    val authorCrop: String = "",
    val question: String,
    val imageUrl: String = "",              // Optional attached image
    val expertAnswer: String = "",
    val likes: Int = 0,
    val replies: Int = 0,
    val tags: String = "",                 // Comma-separated: "maize,pest,irrigation"
    val isAnswered: Boolean = false,
    val isUserPost: Boolean = false,       // true = posted by current farmer
    val postedAt: Long = System.currentTimeMillis()
)
