package com.example.agri_sense.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.local.AgriSenseDataStore
import com.example.agri_sense.data.models.Farmer
import com.example.agri_sense.data.network.*
import com.example.agri_sense.data.repository.FarmerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository,
    private val authApi: AuthApi,
    private val dataStore: AgriSenseDataStore
) : ViewModel() {

    private val _isOnboarded = MutableStateFlow<Boolean?>(null)
    val isOnboarded: StateFlow<Boolean?> = _isOnboarded.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _otpDispatched = MutableStateFlow<String?>(null)
    val otpDispatched: StateFlow<String?> = _otpDispatched.asStateFlow()

    private val _passwordResetSuccess = MutableStateFlow(false)
    val passwordResetSuccess: StateFlow<Boolean> = _passwordResetSuccess.asStateFlow()

    private val _isProfileComplete = MutableStateFlow<Boolean?>(null)
    val isProfileComplete: StateFlow<Boolean?> = _isProfileComplete.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val token = dataStore.authToken.first()
            if (token != null) {
                _isOnboarded.value = true
                _isPremium.value = farmerRepository.isPremium()
                _isProfileComplete.value = farmerRepository.getFarmerOnce()?.isProfileComplete == true
            } else {
                _isOnboarded.value = false
                _isProfileComplete.value = false
            }
        }
    }

    fun signUp(phone: String, name: String, password: String, language: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = authApi.phoneSignup(PhoneSignupRequest(phone, name, password, language))
                if (response.isSuccessful) {
                    _otpDispatched.value = response.body()?.otp
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        org.json.JSONObject(errorBody ?: "").getString("error")
                    } catch (e: Exception) {
                        "Registration failed. Phone might be already registered."
                    }
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun signIn(phone: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = authApi.phoneSignin(PhoneSigninRequest(phone, password))
                if (response.isSuccessful) {
                    val auth = response.body()!!
                    dataStore.setAuthToken(auth.token)
                    dataStore.setUserId(auth.user.id)
                    
                    // Sync local DB with backend data
                    farmerRepository.clearAll() // Ensure no stale data
                    farmerRepository.saveFarmer(Farmer(
                        name = auth.user.name,
                        phone = auth.user.phone,
                        preferredLanguage = auth.user.language,
                        isOnboarded = true,
                        isProfileComplete = auth.user.isProfileComplete ?: false,
                        district = auth.user.district ?: "Lilongwe",
                        farmSize = auth.user.farmSize ?: 1.0,
                        cropsGrown = auth.user.cropsGrown ?: "Maize"
                    ))
                    _isProfileComplete.value = auth.user.isProfileComplete ?: false
                    _isOnboarded.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        org.json.JSONObject(errorBody ?: "").getString("error")
                    } catch (e: Exception) {
                        "Login failed. Check credentials."
                    }
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun requestPhoneOtp(phone: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = authApi.requestPhoneOtp(PhoneRequest(phone))
                if (response.isSuccessful) {
                    _otpDispatched.value = response.body()?.otp
                } else {
                    _error.value = "Login failed."
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun verifyOtp(phone: String, code: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = authApi.verifyPhoneOtp(VerifyOtpRequest(phone, code))
                if (response.isSuccessful) {
                    val auth = response.body()!!
                    dataStore.setAuthToken(auth.token)
                    dataStore.setUserId(auth.user.id)
                    
                    // Sync local DB with backend data
                    farmerRepository.clearAll() // Ensure no stale data
                    farmerRepository.saveFarmer(Farmer(
                        name = auth.user.name,
                        phone = auth.user.phone,
                        preferredLanguage = auth.user.language,
                        isOnboarded = true,
                        isProfileComplete = auth.user.isProfileComplete ?: false,
                        district = auth.user.district ?: "Lilongwe",
                        farmSize = auth.user.farmSize ?: 1.0,
                        cropsGrown = auth.user.cropsGrown ?: "Maize"
                    ))
                    _isProfileComplete.value = auth.user.isProfileComplete ?: false
                    _isOnboarded.value = true
                } else {
                    _error.value = "Invalid OTP."
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetPassword(phone: String, code: String, newPassword: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = authApi.resetPassword(ResetPasswordRequest(phone, code, newPassword))
                if (response.isSuccessful) {
                    _passwordResetSuccess.value = true
                    _otpDispatched.value = null
                } else {
                    _error.value = "Reset failed. Check OTP."
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearOtp() {
        _otpDispatched.value = null
    }

    fun clearError() {
        _error.value = null
    }

    fun selectPlan(planName: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val userId = dataStore.userId.first() ?: return@launch
                val response = authApi.selectPlan(SelectPlanRequest(userId, planName))
                if (response.isSuccessful) {
                    if (planName == "FREE") {
                        // User proceeds to profile setup, so no state change needed here.
                        // Navigation is handled in the UI.
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun verifyPayment(subscriptionId: String, paymentId: String, provider: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = authApi.verifyPayment(VerifyPaymentRequest(subscriptionId, paymentId, provider))
                if (response.isSuccessful) {
                    val farmer = farmerRepository.getFarmerOnce()!!
                    farmerRepository.activatePremium(farmer)
                    _isPremium.value = true
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.setAuthToken(null)
            dataStore.setUserId(null)
            farmerRepository.clearAll() // Clear local identity on logout
            _isOnboarded.value = false
            _isPremium.value = false
            _isProfileComplete.value = false
        }
    }

    fun saveProfile(
        name: String,
        phone: String,
        district: String,
        farmSize: Double,
        crops: List<String>,
        language: String,
        subscriptionStatus: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val currentFarmer = farmerRepository.getFarmerOnce()
                val finalPhone = if (phone.isBlank()) currentFarmer?.phone ?: "" else phone

                // 1. Update Backend
                val backendResponse = authApi.updateProfile(UpdateProfileRequest(
                    phone = finalPhone,
                    district = district,
                    farmSize = farmSize,
                    cropsGrown = crops.joinToString(","),
                    language = language,
                    isProfileComplete = true
                ))

                if (backendResponse.isSuccessful) {
                    // 2. Update Local Room DB
                    if (currentFarmer != null) {
                        farmerRepository.updateFarmer(currentFarmer.copy(
                            name = name,
                            district = district,
                            farmSize = farmSize,
                            cropsGrown = crops.joinToString(","),
                            preferredLanguage = language,
                            subscriptionStatus = subscriptionStatus,
                            isProfileComplete = true
                        ))
                    } else {
                        farmerRepository.saveFarmer(Farmer(
                            name = name,
                            phone = phone,
                            district = district,
                            farmSize = farmSize,
                            cropsGrown = crops.joinToString(","),
                            preferredLanguage = language,
                            subscriptionStatus = subscriptionStatus,
                            isOnboarded = true,
                            isProfileComplete = true
                        ))
                    }
                    _isProfileComplete.value = true
                } else {
                    _error.value = "Failed to sync profile with server."
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun activatePremium() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val farmer = farmerRepository.getFarmerOnce()
                if (farmer != null) {
                    farmerRepository.activatePremium(farmer)
                    _isPremium.value = true
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
