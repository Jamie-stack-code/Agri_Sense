package com.example.agri_sense.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.Farmer
import com.example.agri_sense.data.repository.FarmerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository
) : ViewModel() {

    val farmer: StateFlow<Farmer?> = farmerRepository.farmerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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

