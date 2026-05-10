package com.example.agri_sense.di

import com.example.agri_sense.data.network.OpenMeteoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenMeteoApi(okHttpClient: OkHttpClient): OpenMeteoApi {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(okHttpClient: OkHttpClient): com.example.agri_sense.data.network.AuthApi {
        return Retrofit.Builder()
            .baseUrl(com.example.agri_sense.BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.example.agri_sense.data.network.AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideIntelApi(okHttpClient: OkHttpClient): com.example.agri_sense.data.network.IntelApi {
        return Retrofit.Builder()
            .baseUrl(com.example.agri_sense.BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.example.agri_sense.data.network.IntelApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommunityApi(okHttpClient: OkHttpClient): com.example.agri_sense.data.network.CommunityApi {
        return Retrofit.Builder()
            .baseUrl(com.example.agri_sense.BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.example.agri_sense.data.network.CommunityApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSoilApi(okHttpClient: OkHttpClient): com.example.agri_sense.data.network.SoilApi {
        return Retrofit.Builder()
            .baseUrl(com.example.agri_sense.BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.example.agri_sense.data.network.SoilApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarketApi(okHttpClient: OkHttpClient): com.example.agri_sense.data.network.MarketApi {
        return Retrofit.Builder()
            .baseUrl(com.example.agri_sense.BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.example.agri_sense.data.network.MarketApi::class.java)
    }
}
