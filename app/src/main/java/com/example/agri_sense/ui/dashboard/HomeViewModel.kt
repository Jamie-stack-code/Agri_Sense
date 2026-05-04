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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository,
    private val weatherRepository: WeatherRepository,
    private val pestRepository: PestAlertRepository,
    private val discussionRepository: DiscussionRepository
) : ViewModel() {

    val farmer: StateFlow<Farmer?> = farmerRepository.farmerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val unreadPestCount: StateFlow<Int> = pestRepository.unreadCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _weather = MutableStateFlow<WeatherAlert?>(null)
    val weather: StateFlow<WeatherAlert?> = _weather.asStateFlow()

    private val _recentDiscussions = MutableStateFlow<List<Discussion>>(emptyList())
    val recentDiscussions: StateFlow<List<Discussion>> = _recentDiscussions.asStateFlow()

    private val _aiResponse = MutableStateFlow("")
    val aiResponse: StateFlow<String> = _aiResponse.asStateFlow()

    init {
        viewModelScope.launch {
            // Seed all data sources on first launch
            weatherRepository.seedIfEmpty()
            pestRepository.seedIfEmpty()
            discussionRepository.seedIfEmpty()
        }
        observeWeather()
        observeDiscussions()
    }

    private fun observeWeather() {
        viewModelScope.launch {
            farmer.collect { f ->
                val district = f?.district ?: "Lilongwe"
                weatherRepository.getWeatherForDistrict(district).collect { w ->
                    _weather.value = w
                }
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

    /** Simulated AI chat response based on keyword matching */
    fun askAgriSense(question: String, isEnglish: Boolean) {
        viewModelScope.launch {
            val q = question.lowercase()
            _aiResponse.value = when {
                q.contains("maize") && q.contains("yellow") ->
                    if (isEnglish) "Yellow leaves on maize usually indicate nitrogen deficiency. Apply CAN fertilizer at 50 kg/ha as a top-dress. Ensure adequate soil moisture before application."
                    else "Masamba ofira a chimanga amasonyeza kuchepa kwa naitorojeni. Gwiritsani ntchito feteleza ya CAN pa 50 kg/ha."
                q.contains("pest") || q.contains("insect") || q.contains("dzombe") ->
                    if (isEnglish) "For pest identification, use the Soil & Camera feature to capture photos. Check the Community Pest Alerts tab for current outbreaks in your district."
                    else "Kuti muwone mtundu wa dzombe, gwiritsani ntchito kamera. Onani Zibuyo za Tizilombo m'chigawo chanu."
                q.contains("rain") || q.contains("weather") || q.contains("mvula") ->
                    if (isEnglish) "Check your weather card above for today's forecast. Kasungu and Northern regions currently have storm warnings."
                    else "Onani khadi la nyengo pamwamba. Kasungu ndi Kumpoto kuna chenjezero cha mphenzi."
                q.contains("market") || q.contains("price") || q.contains("mtengo") ->
                    if (isEnglish) "Current best prices: Groundnuts MK 1,520/kg (Zomba), Tobacco MK 3,200/kg (Mzuzu). Visit the Market tab for real-time prices near you."
                    else "Mitengo yabwino: Mtedza MK 1,520/kg (Zomba), Fodya MK 3,200/kg (Mzuzu). Onani Msika kwa mitengo yeniyeni."
                q.contains("fertilizer") || q.contains("feteleza") ->
                    if (isEnglish) "For basal fertilizer, use NPK 23-21-0+4S at 200 kg/ha at planting. Top-dress with CAN at 200 kg/ha at 4-6 weeks after emergence."
                    else "Kwa feteleza yoyamba, gwiritsani ntchito NPK 23-21-0+4S pa 200 kg/ha posenza. Onjezani CAN pa 200 kg/ha patatha milungu 4-6."
                else ->
                    if (isEnglish) "I'm Agri-Sense AI, your smart farming assistant. Ask me about crop diseases, market prices, weather, soil management, or fertilizer recommendations."
                    else "Ine ndine Agri-Sense AI, wothandiza wanu wolima. Ndifunseni za matenda a mbewu, mitengo ya msika, nyengo, nthaka, kapena feteleza."
            }
        }
    }

    fun clearAiResponse() { _aiResponse.value = "" }
}
