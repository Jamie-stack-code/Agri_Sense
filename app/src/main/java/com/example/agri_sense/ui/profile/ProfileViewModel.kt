package com.example.agri_sense.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.Farmer
import com.example.agri_sense.data.repository.FarmerRepository
import com.example.agri_sense.data.network.CommunityApi
import com.example.agri_sense.data.network.DiagnosticReportResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository,
    private val marketRepository: com.example.agri_sense.data.repository.MarketRepository,
    private val communityApi: CommunityApi
) : ViewModel() {

    val farmer: StateFlow<Farmer?> = farmerRepository.farmerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeAlertCount: StateFlow<Int> = farmerRepository.farmerFlow
        .map { f ->
            if (f == null) 0 else marketRepository.getFarmerAlerts(f.id.toString()).size
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Reactive — re-emits whenever the DB row changes (e.g., after upgrade) */
    val isPremium: StateFlow<Boolean> = farmerRepository.farmerFlow
        .map { f ->
            f != null &&
            f.subscriptionStatus == "PREMIUM" &&
            f.subscriptionExpiry > System.currentTimeMillis()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    /** Days remaining on current subscription / trial.
     *  Returns 30 (default trial length) when no expiry has been set yet.  */
    val daysRemaining: StateFlow<Int> = farmerRepository.farmerFlow
        .map { f ->
            if (f == null) return@map 0
            if (f.subscriptionExpiry == 0L) {
                // free trial seeded during saveProfile — compute from createdAt + 30d
                val trialEnd = f.createdAt + (30L * 24 * 60 * 60 * 1000)
                ((trialEnd - System.currentTimeMillis()) / (24 * 60 * 60 * 1000L))
                    .toInt().coerceAtLeast(0)
            } else {
                ((f.subscriptionExpiry - System.currentTimeMillis()) / (24 * 60 * 60 * 1000L))
                    .toInt().coerceAtLeast(0)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 30)

    private val _diagnosticHistory = MutableStateFlow<List<DiagnosticReportResponse>>(emptyList())
    val diagnosticHistory: StateFlow<List<DiagnosticReportResponse>> = _diagnosticHistory.asStateFlow()

    init {
        fetchDiagnosticHistory()
    }

    private fun fetchDiagnosticHistory() {
        viewModelScope.launch {
            try {
                val current = farmerRepository.getFarmerOnce()
                if (current != null) {
                    val history = communityApi.getDiagnosticHistory(current.id.toString())
                    _diagnosticHistory.value = history
                }
            } catch (e: Exception) {
                // Keep empty
            }
        }
    }

    fun updateProfile(
        name: String,
        phone: String,
        district: String,
        crops: List<String>,
        farmSizeHa: Double
    ) {
        viewModelScope.launch {
            val current = farmerRepository.getFarmerOnce() ?: return@launch
            farmerRepository.updateFarmer(
                current.copy(
                    name = name.trim(),
                    phone = phone.trim(),
                    district = district,
                    cropsGrown = crops.joinToString(","),
                    farmSize = farmSizeHa
                )
            )
        }
    }

    fun updateLanguage(language: String) {
        viewModelScope.launch {
            val current = farmerRepository.getFarmerOnce() ?: return@launch
            farmerRepository.updateFarmer(current.copy(preferredLanguage = language))
        }
    }

    fun updateAvatarUri(uri: String) {
        viewModelScope.launch {
            val current = farmerRepository.getFarmerOnce() ?: return@launch
            farmerRepository.updateFarmer(current.copy(avatarUri = uri))
        }
    }

    /** Returns the farmer's total active days since profile creation */
    fun getActiveDays(): Int {
        val createdAt = farmer.value?.createdAt ?: return 0
        return ((System.currentTimeMillis() - createdAt) / (24 * 60 * 60 * 1000L)).toInt()
    }
}

