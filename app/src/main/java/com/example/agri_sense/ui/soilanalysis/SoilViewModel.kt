package com.example.agri_sense.ui.soilanalysis

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.SoilAnalysis
import com.example.agri_sense.data.repository.FarmerRepository
import com.example.agri_sense.data.repository.SoilRepository
import com.example.agri_sense.ml.SoilClassifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoilViewModel @Inject constructor(
    private val soilRepository: SoilRepository,
    private val farmerRepository: FarmerRepository
) : ViewModel() {

    val allAnalyses: StateFlow<List<SoilAnalysis>> = soilRepository.getAllAnalyses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            farmerRepository.farmerFlow.collect { farmer ->
                farmer?.id?.let { id ->
                    soilRepository.syncHistory(id.toString())
                }
            }
        }
    }

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _latestResult = MutableStateFlow<SoilClassifier.SoilResult?>(null)
    val latestResult: StateFlow<SoilClassifier.SoilResult?> = _latestResult.asStateFlow()

    private val _qualityScore = MutableStateFlow(0)
    val qualityScore: StateFlow<Int> = _qualityScore.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun runAnalysis(dryUri: Uri, wetUri: Uri) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _error.value = null
            try {
                val farmerId = farmerRepository.getFarmerOnce()?.id ?: 0
                val result = soilRepository.runAndSaveAnalysis(farmerId, dryUri, wetUri)
                _latestResult.value = result
                
                // Calculate quality score for the gauge
                var score = 70
                if (result.properties.pH in 6.0..7.0) score += 15
                if (result.properties.nitrogen != "Low") score += 10
                _qualityScore.value = score.coerceAtMost(100)
            } catch (e: Exception) {
                _error.value = "Analysis failed: ${e.message}"
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun clearError() { _error.value = null }
}
