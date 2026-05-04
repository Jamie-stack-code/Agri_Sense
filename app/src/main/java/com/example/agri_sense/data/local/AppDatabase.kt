package com.example.agri_sense.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.agri_sense.data.local.dao.*
import com.example.agri_sense.data.models.*

@Database(
    entities = [
        Farmer::class,
        SoilAnalysis::class,
        MarketPrice::class,
        PestAlert::class,
        Discussion::class,
        WeatherAlert::class,
        CropRecommendation::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun farmerDao(): FarmerDao
    abstract fun soilAnalysisDao(): SoilAnalysisDao
    abstract fun marketPriceDao(): MarketPriceDao
    abstract fun pestAlertDao(): PestAlertDao
    abstract fun discussionDao(): DiscussionDao
    abstract fun weatherAlertDao(): WeatherAlertDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        /** Migrates v1 → v2: adds all new tables and new columns on farmers */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Farmer table — add new columns
                db.execSQL("ALTER TABLE farmers ADD COLUMN phone TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE farmers ADD COLUMN region TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE farmers ADD COLUMN farmSizeUnit TEXT NOT NULL DEFAULT 'Ha'")
                db.execSQL("ALTER TABLE farmers ADD COLUMN subscriptionStatus TEXT NOT NULL DEFAULT 'FREE'")
                db.execSQL("ALTER TABLE farmers ADD COLUMN subscriptionExpiry INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE farmers ADD COLUMN avatarUri TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE farmers ADD COLUMN isOnboarded INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE farmers ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")

                // New tables
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS market_prices (
                        id TEXT NOT NULL PRIMARY KEY,
                        cropName TEXT NOT NULL,
                        cropNameChichewa TEXT NOT NULL DEFAULT '',
                        pricePerKg REAL NOT NULL,
                        priceUnit TEXT NOT NULL DEFAULT 'MWK',
                        marketName TEXT NOT NULL,
                        district TEXT NOT NULL,
                        region TEXT NOT NULL DEFAULT '',
                        marketLocationLat REAL NOT NULL,
                        marketLocationLng REAL NOT NULL,
                        trendPercent REAL NOT NULL DEFAULT 0.0,
                        lastUpdated INTEGER NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS pest_alerts (
                        id TEXT NOT NULL PRIMARY KEY,
                        pestName TEXT NOT NULL,
                        pestNameChichewa TEXT NOT NULL DEFAULT '',
                        affectedCrops TEXT NOT NULL,
                        outbreakDistricts TEXT NOT NULL,
                        severityLevel TEXT NOT NULL,
                        description TEXT NOT NULL,
                        descriptionChichewa TEXT NOT NULL DEFAULT '',
                        recommendedAction TEXT NOT NULL,
                        recommendedActionChichewa TEXT NOT NULL DEFAULT '',
                        imageRes TEXT NOT NULL DEFAULT '',
                        reportedAt INTEGER NOT NULL,
                        isRead INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS discussions (
                        id TEXT NOT NULL PRIMARY KEY,
                        authorName TEXT NOT NULL,
                        authorDistrict TEXT NOT NULL,
                        authorCrop TEXT NOT NULL DEFAULT '',
                        question TEXT NOT NULL,
                        expertAnswer TEXT NOT NULL DEFAULT '',
                        likes INTEGER NOT NULL DEFAULT 0,
                        replies INTEGER NOT NULL DEFAULT 0,
                        tags TEXT NOT NULL DEFAULT '',
                        isAnswered INTEGER NOT NULL DEFAULT 0,
                        isUserPost INTEGER NOT NULL DEFAULT 0,
                        postedAt INTEGER NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS weather_alerts (
                        id TEXT NOT NULL PRIMARY KEY,
                        district TEXT NOT NULL,
                        condition TEXT NOT NULL,
                        conditionChichewa TEXT NOT NULL DEFAULT '',
                        temperatureC REAL NOT NULL,
                        temperatureHigh REAL NOT NULL DEFAULT 0.0,
                        temperatureLow REAL NOT NULL DEFAULT 0.0,
                        humidity INTEGER NOT NULL,
                        rainfallMm REAL NOT NULL DEFAULT 0.0,
                        windSpeedKmh REAL NOT NULL DEFAULT 0.0,
                        uvIndex INTEGER NOT NULL DEFAULT 3,
                        severeWarning INTEGER NOT NULL DEFAULT 0,
                        warningMessage TEXT NOT NULL DEFAULT '',
                        warningMessageChichewa TEXT NOT NULL DEFAULT '',
                        validFrom INTEGER NOT NULL,
                        validTo INTEGER NOT NULL,
                        fetchedAt INTEGER NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS crop_recommendations (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        soilAnalysisId INTEGER NOT NULL,
                        cropName TEXT NOT NULL,
                        cropNameChichewa TEXT NOT NULL DEFAULT '',
                        confidenceScore REAL NOT NULL,
                        reasonSummary TEXT NOT NULL,
                        plantingGuide TEXT NOT NULL,
                        fertilizerAdvice TEXT NOT NULL,
                        wateringNeeds TEXT NOT NULL,
                        expectedYieldKgPerHa REAL NOT NULL DEFAULT 0.0,
                        createdAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "agri_sense_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
