package com.example.agri_sense.di

import android.content.Context
import com.example.agri_sense.data.local.AppDatabase
import com.example.agri_sense.data.local.dao.DiscussionDao
import com.example.agri_sense.data.local.dao.FarmerDao
import com.example.agri_sense.data.local.dao.MarketPriceDao
import com.example.agri_sense.data.local.dao.PestAlertDao
import com.example.agri_sense.data.local.dao.SoilAnalysisDao
import com.example.agri_sense.data.local.dao.WeatherAlertDao
import com.example.agri_sense.ml.SoilClassifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideFarmerDao(db: AppDatabase): FarmerDao = db.farmerDao()

    @Provides
    @Singleton
    fun provideSoilAnalysisDao(db: AppDatabase): SoilAnalysisDao = db.soilAnalysisDao()

    @Provides
    @Singleton
    fun provideMarketPriceDao(db: AppDatabase): MarketPriceDao = db.marketPriceDao()

    @Provides
    @Singleton
    fun providePestAlertDao(db: AppDatabase): PestAlertDao = db.pestAlertDao()

    @Provides
    @Singleton
    fun provideDiscussionDao(db: AppDatabase): DiscussionDao = db.discussionDao()

    @Provides
    @Singleton
    fun provideWeatherAlertDao(db: AppDatabase): WeatherAlertDao = db.weatherAlertDao()

    @Provides
    @Singleton
    fun provideSoilClassifier(@ApplicationContext context: Context): SoilClassifier =
        SoilClassifier(context)
}
