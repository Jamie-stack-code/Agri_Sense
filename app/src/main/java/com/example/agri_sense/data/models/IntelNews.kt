package com.example.agri_sense.data.models

data class IntelNews(
    val id: String,
    val tag: String,
    val tagColor: String,
    val title: String,
    val body: String,
    val titleChichewa: String,
    val bodyChichewa: String,
    val timestamp: String? = null
)
