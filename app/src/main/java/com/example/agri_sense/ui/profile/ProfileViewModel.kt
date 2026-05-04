package com.example.agri_sense.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.Farmer
import com.example.agri_sense.data.repository.FarmerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository
) : ViewModel() {

    val farmer: StateFlow<Farmer?> = farmerRepository.farmerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isPremium: StateFlow<Boolean> = kotlinx.coroutines.flow.flow {
        emit(farmerRepository.isPremium())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

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
