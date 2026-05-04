package com.example.agri_sense.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.local.AgriSenseDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: AgriSenseDataStore
) : ViewModel() {

    val language: StateFlow<String> = dataStore.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "English")

    val offlineMode: StateFlow<Boolean> = dataStore.offlineMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val notifyPest: StateFlow<Boolean> = dataStore.notifyPest
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notifyMarket: StateFlow<Boolean> = dataStore.notifyMarket
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notifyWeather: StateFlow<Boolean> = dataStore.notifyWeather
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setLanguage(lang: String) = viewModelScope.launch { dataStore.setLanguage(lang) }

    fun setOfflineMode(enabled: Boolean) = viewModelScope.launch { dataStore.setOfflineMode(enabled) }

    fun setNotifyPest(enabled: Boolean) = viewModelScope.launch { dataStore.setNotifyPest(enabled) }

    fun setNotifyMarket(enabled: Boolean) = viewModelScope.launch { dataStore.setNotifyMarket(enabled) }

    fun setNotifyWeather(enabled: Boolean) = viewModelScope.launch { dataStore.setNotifyWeather(enabled) }
}
