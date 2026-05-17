package com.example.agri_sense.ui.onboarding

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.local.AgriSenseDataStore
import com.example.agri_sense.data.models.Farmer
import com.example.agri_sense.data.network.*
import com.example.agri_sense.data.repository.FarmerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
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

    // Holds the verification ID returned by Firebase after sending the SMS OTP
    private val _verificationId = MutableStateFlow<String?>(null)
    val verificationId: StateFlow<String?> = _verificationId.asStateFlow()

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

    fun signInWithPassword(phone: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val cleanPhone = phone.trim().replace(Regex("[^0-9+]"), "")
                val formattedPhone = if (cleanPhone.startsWith("+")) {
                    cleanPhone
                } else if (cleanPhone.startsWith("0")) {
                    "+265" + cleanPhone.substring(1)
                } else {
                    "+265$cleanPhone"
                }

                val response = authApi.phoneSignin(PhoneSigninRequest(formattedPhone, password))
                if (response.isSuccessful) {
                    val auth = response.body()!!
                    dataStore.setAuthToken(auth.token)
                    dataStore.setUserId(auth.user.id)

                    farmerRepository.clearAll()
                    farmerRepository.saveFarmer(Farmer(
                        name = auth.user.name ?: "Farmer",
                        phone = auth.user.phone ?: formattedPhone,
                        preferredLanguage = auth.user.language ?: "English",
                        isOnboarded = true,
                        isProfileComplete = auth.user.isProfileComplete ?: false,
                        district = auth.user.district ?: "Lilongwe",
                        farmSize = auth.user.farmSize ?: 1.0,
                        cropsGrown = auth.user.cropsGrown ?: "Maize"
                    ))
                    _isProfileComplete.value = auth.user.isProfileComplete ?: false
                    _isOnboarded.value = true
                } else {
                    _error.value = "Sign in failed. Please check your credentials."
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Sign In Exception", e)
                _error.value = "Sign In Error: ${e.localizedMessage ?: e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun signInWithFirebaseCredential(credential: com.google.firebase.auth.PhoneAuthCredential) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance()

                // 1. Sign in with Firebase using .await() extension
                val authResult = firebaseAuth.signInWithCredential(credential).await()

                // 2. Get ID Token using .await() extension
                val idToken = authResult.user?.getIdToken(true)?.await()?.token

                if (idToken != null) {
                    // 3. Sync with NestJS Backend
                    val response = authApi.verifyToken("Bearer $idToken")
                    if (response.isSuccessful) {
                        val auth = response.body()!!
                        dataStore.setAuthToken(auth.token)
                        dataStore.setUserId(auth.user.id)

                        farmerRepository.clearAll()
                        farmerRepository.saveFarmer(Farmer(
                            name = auth.user.name ?: "Farmer",
                            phone = auth.user.phone ?: "",
                            preferredLanguage = auth.user.language ?: "English",
                            isOnboarded = true,
                            isProfileComplete = auth.user.isProfileComplete ?: false,
                            district = auth.user.district ?: "Lilongwe",
                            farmSize = auth.user.farmSize ?: 1.0,
                            cropsGrown = auth.user.cropsGrown ?: "Maize"
                        ))
                        _isProfileComplete.value = auth.user.isProfileComplete ?: false
                        _isOnboarded.value = true
                    } else {
                        _error.value = "Backend Sync Failed."
                    }
                } else {
                    _error.value = "Could not retrieve Firebase token."
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Firebase Login Exception", e)
                _error.value = "Firebase Login Error: ${e.localizedMessage ?: e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Step 1 — Send SMS OTP via Firebase Phone Auth.
     * Call this from SignUpScreen and SignInScreen when user taps Send OTP.
     */
    fun sendPhoneOtp(phone: String, activity: Activity) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val cleanPhone = phone.trim().replace(Regex("[^0-9+]"), "")
        val formattedPhone = if (cleanPhone.startsWith("+")) {
            cleanPhone
        } else if (cleanPhone.startsWith("0")) {
            "+265" + cleanPhone.substring(1)
        } else {
            "+265$cleanPhone"
        }
        _loading.value = true
        _error.value = null

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto-retrieval or instant verification — sign in directly
                signInWithFirebaseCredential(credential)
            }

            override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                _loading.value = false
                _error.value = "OTP Failed: ${e.message}"
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                _loading.value = false
                _verificationId.value = verificationId
                _otpDispatched.value = "sent" // Signal UI to navigate to OTP screen
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(formattedPhone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * Step 2 — Verify the SMS code entered by the user.
     * Call this from OtpVerificationScreen.
     */
    fun verifyOtp(phone: String, code: String) {
        val vId = _verificationId.value
        if (vId == null) {
            _error.value = "Session expired. Please go back and request the code again."
            return
        }
        val credential = PhoneAuthProvider.getCredential(vId, code)
        signInWithFirebaseCredential(credential)
    }

    // Legacy stubs — no longer used but kept for API compatibility
    fun signUp(phone: String, name: String, password: String, language: String) {
        _error.value = "Please use sendPhoneOtp() to start phone verification."
    }

    fun signIn(phone: String, password: String) {
        _error.value = "Please use sendPhoneOtp() to start phone verification."
    }

    fun requestPhoneOtp(phone: String) {
        _error.value = "Please use sendPhoneOtp() with an Activity reference."
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
