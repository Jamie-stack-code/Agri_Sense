package com.example.agri_sense.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.Farmer
import com.example.agri_sense.data.models.WeatherAlert
import com.example.agri_sense.data.repository.FarmerRepository
import com.example.agri_sense.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val weather: StateFlow<WeatherAlert?> = farmerRepository.farmerFlow
        .flatMapLatest { farmer ->
            val district = farmer?.district ?: "Lilongwe"
            // Start a side-effect for sync, but return the local DB flow
            viewModelScope.launch {
                weatherRepository.syncWeatherForDistrict(district)
            }
            weatherRepository.getWeatherForDistrict(district)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        // ViewModel is already observing via stateIn
    }
}
