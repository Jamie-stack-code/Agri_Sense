package com.example.agri_sense.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.Farmer
import com.example.agri_sense.data.repository.FarmerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository
) : ViewModel() {

    private val _isOnboarded = MutableStateFlow<Boolean?>(null) // null = loading
    val isOnboarded: StateFlow<Boolean?> = _isOnboarded.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            _isOnboarded.value = farmerRepository.isOnboarded()
            _isPremium.value = farmerRepository.isPremium()
        }
    }

    fun saveProfile(
        name: String,
        phone: String,
        district: String,
        farmSize: Double,
        crops: List<String>,
        language: String,
        subscriptionStatus: String = "FREE"
    ) {
        viewModelScope.launch {
            farmerRepository.saveFarmer(
                Farmer(
                    name = name,
                    phone = phone,
                    district = district,
                    region = getRegionForDistrict(district),
                    farmSize = farmSize,
                    cropsGrown = crops.joinToString(","),
                    preferredLanguage = language,
                    subscriptionStatus = subscriptionStatus,
                    isOnboarded = true
                )
            )
            _isOnboarded.value = true
        }
    }

    fun activatePremium() {
        viewModelScope.launch {
            val farmer = farmerRepository.getFarmerOnce() ?: return@launch
            farmerRepository.activatePremium(farmer)
            _isPremium.value = true
        }
    }

    fun updatePhone(phone: String) {
        viewModelScope.launch {
            val farmer = farmerRepository.getFarmerOnce() ?: return@launch
            farmerRepository.updateFarmer(farmer.copy(phone = phone))
        }
    }

    private fun getRegionForDistrict(district: String): String = when (district) {
        "Karonga", "Chitipa", "Rumphi", "Mzimba", "Nkhata Bay", "Likoma" -> "Northern"
        "Kasungu", "Nkhotakota", "Ntchisi", "Dowa", "Salima", "Lilongwe",
        "Mchinji", "Dedza", "Ntcheu" -> "Central"
        else -> "Southern"
    }
}
