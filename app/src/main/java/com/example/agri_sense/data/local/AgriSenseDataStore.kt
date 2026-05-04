package com.example.agri_sense.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "agri_sense_prefs")

@Singleton
class AgriSenseDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val KEY_LANGUAGE      = stringPreferencesKey("language")
        val KEY_OFFLINE_MODE  = booleanPreferencesKey("offline_mode")
        val KEY_NOTIFY_PEST   = booleanPreferencesKey("notify_pest")
        val KEY_NOTIFY_MARKET = booleanPreferencesKey("notify_market")
        val KEY_NOTIFY_WEATHER = booleanPreferencesKey("notify_weather")
        val KEY_LAST_SYNC     = stringPreferencesKey("last_sync_timestamp")
    }

    val language: Flow<String> = context.dataStore.data.map { it[KEY_LANGUAGE] ?: "English" }
    val offlineMode: Flow<Boolean> = context.dataStore.data.map { it[KEY_OFFLINE_MODE] ?: false }
    val notifyPest: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIFY_PEST] ?: true }
    val notifyMarket: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIFY_MARKET] ?: true }
    val notifyWeather: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIFY_WEATHER] ?: true }
    val lastSyncTimestamp: Flow<String> = context.dataStore.data.map { it[KEY_LAST_SYNC] ?: "" }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[KEY_LANGUAGE] = lang }
    }
    suspend fun setOfflineMode(enabled: Boolean) {
        context.dataStore.edit { it[KEY_OFFLINE_MODE] = enabled }
    }
    suspend fun setNotifyPest(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIFY_PEST] = enabled }
    }
    suspend fun setNotifyMarket(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIFY_MARKET] = enabled }
    }
    suspend fun setNotifyWeather(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIFY_WEATHER] = enabled }
    }
    suspend fun setLastSyncTimestamp(ts: String) {
        context.dataStore.edit { it[KEY_LAST_SYNC] = ts }
    }
}
