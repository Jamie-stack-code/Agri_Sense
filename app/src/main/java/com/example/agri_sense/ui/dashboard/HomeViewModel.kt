package com.example.agri_sense.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.Discussion
import com.example.agri_sense.data.models.Farmer
import com.example.agri_sense.data.models.WeatherAlert
import com.example.agri_sense.data.repository.DiscussionRepository
import com.example.agri_sense.data.repository.FarmerRepository
import com.example.agri_sense.data.repository.PestAlertRepository
import com.example.agri_sense.data.repository.WeatherRepository
import com.example.agri_sense.data.repository.AiExpertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

import com.example.agri_sense.data.models.IntelNews
import com.example.agri_sense.data.network.IntelApi

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository,
    private val weatherRepository: WeatherRepository,
    private val pestRepository: PestAlertRepository,
    private val discussionRepository: DiscussionRepository,
    private val aiExpertRepository: AiExpertRepository,
    private val intelApi: IntelApi
) : ViewModel() {

    val farmer: StateFlow<Farmer?> = farmerRepository.farmerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val unreadPestCount: StateFlow<Int> = pestRepository.unreadCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _weather = MutableStateFlow<WeatherAlert?>(null)
    val weather: StateFlow<WeatherAlert?> = _weather.asStateFlow()

    private val _recentDiscussions = MutableStateFlow<List<Discussion>>(emptyList())
    val recentDiscussions: StateFlow<List<Discussion>> = _recentDiscussions.asStateFlow()

    private val _liveNews = MutableStateFlow<List<IntelNews>>(emptyList())
    val liveNews: StateFlow<List<IntelNews>> = _liveNews.asStateFlow()

    private val _aiResponse = MutableStateFlow("")
    val aiResponse: StateFlow<String> = _aiResponse.asStateFlow()

    init {
        viewModelScope.launch {
            // Seed all data sources on first launch
            weatherRepository.seedIfEmpty()
            pestRepository.seedIfEmpty()
            discussionRepository.seedIfEmpty()
            fetchLiveNews()
        }
        observeWeather()
        observeDiscussions()
    }

    private suspend fun fetchLiveNews() {
        try {
            val response = intelApi.getIntelNews()
            if (response.isSuccessful) {
                _liveNews.value = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeWeather() {
        viewModelScope.launch {
            farmer.flatMapLatest { f ->
                val district = f?.district ?: "Lilongwe"
                viewModelScope.launch {
                    weatherRepository.syncWeatherForDistrict(district)
                }
                weatherRepository.getWeatherForDistrict(district)
            }.collect { w ->
                _weather.value = w
            }
        }
    }

    private fun observeDiscussions() {
        viewModelScope.launch {
            discussionRepository.answeredDiscussions.collect { list ->
                _recentDiscussions.value = list.take(3)
            }
        }
    }

    /** Generative AI chat response */
    fun askAgriSense(question: String, isEnglish: Boolean) {
        viewModelScope.launch {
            _aiResponse.value = if (isEnglish) "Consulting agricultural intelligence..." else "Ndikufunsa chidziwitso chauLimi..."
            val response = aiExpertRepository.getAdvice(question, isEnglish)
            _aiResponse.value = response
        }
    }

    fun clearAiResponse() { _aiResponse.value = "" }
}
