package com.example.agri_sense.ui.community

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.network.CommunityApi
import com.example.agri_sense.data.network.DiagnosticReportRequest
import com.example.agri_sense.data.network.DiagnosticReportResponse
import com.example.agri_sense.data.repository.FarmerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PestDiagnosisViewModel @Inject constructor(
    private val communityApi: CommunityApi,
    private val farmerRepository: FarmerRepository
) : ViewModel() {

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _history = MutableStateFlow<List<DiagnosticReportResponse>>(emptyList())
    val history: StateFlow<List<DiagnosticReportResponse>> = _history.asStateFlow()

    fun submitAnalysis(imageUrl: String, cropType: String, aiDiagnosis: String) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                val farmer = farmerRepository.getFarmerOnce()
                if (farmer != null) {
                    communityApi.submitDiagnosticReport(
                        DiagnosticReportRequest(
                            imageUrl = imageUrl,
                            cropType = cropType,
                            aiDiagnosis = aiDiagnosis,
                            farmerId = farmer.id.toString()
                        )
                    )
                }
            } catch (e: Exception) {
                // Log error
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun fetchHistory() {
        viewModelScope.launch {
            try {
                val farmer = farmerRepository.getFarmerOnce()
                if (farmer != null) {
                    val reports = communityApi.getDiagnosticHistory(farmer.id.toString())
                    _history.value = reports
                }
            } catch (e: Exception) {
                // Log error
            }
        }
    }
}
