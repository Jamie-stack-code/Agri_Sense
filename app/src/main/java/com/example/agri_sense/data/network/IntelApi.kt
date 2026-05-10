package com.example.agri_sense.data.network

import com.example.agri_sense.data.models.IntelNews
import retrofit2.Response
import retrofit2.http.GET

interface IntelApi {
    @GET("/api/intelligence/news")
    suspend fun getIntelNews(): Response<List<IntelNews>>
}
