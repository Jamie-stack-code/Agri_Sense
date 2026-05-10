package com.example.agri_sense.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/phone-signup")
    suspend fun phoneSignup(@Body request: PhoneSignupRequest): Response<OtpResponse>

    @POST("auth/phone-signin")
    suspend fun phoneSignin(@Body request: PhoneSigninRequest): Response<AuthResponse>

    @POST("auth/request-phone-otp")
    suspend fun requestPhoneOtp(@Body request: PhoneRequest): Response<OtpResponse>

    @POST("auth/verify-phone-otp")
    suspend fun verifyPhoneOtp(@Body request: VerifyOtpRequest): Response<AuthResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<OtpResponse>

    @POST("auth/update-profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UpdateProfileResponse>

    @POST("subscriptions/select-plan")
    suspend fun selectPlan(@Body request: SelectPlanRequest): Response<SubscriptionResponse>

    @POST("subscriptions/verify-payment")
    suspend fun verifyPayment(@Body request: VerifyPaymentRequest): Response<SubscriptionResponse>
}

data class PhoneSignupRequest(val phone: String, val name: String, val password: String, val language: String)
data class PhoneSigninRequest(val phone: String, val password: String)
data class PhoneRequest(val phone: String)
data class VerifyOtpRequest(val phone: String, val code: String)
data class ResetPasswordRequest(val phone: String, val code: String, val newPassword: String)
data class OtpResponse(val message: String, val otp: String?)
data class AuthResponse(val token: String, val user: FarmerDto)

data class FarmerDto(
    val id: String, 
    val name: String, 
    val phone: String, 
    val role: String, 
    val language: String,
    val isProfileComplete: Boolean? = false,
    val district: String? = null,
    val farmSize: Double? = null,
    val cropsGrown: String? = null
)

data class UpdateProfileRequest(
    val phone: String,
    val district: String,
    val farmSize: Double,
    val cropsGrown: String,
    val language: String,
    val isProfileComplete: Boolean
)

data class UpdateProfileResponse(val message: String, val user: FarmerDto)

data class SelectPlanRequest(val userId: String, val planName: String)
data class SubscriptionResponse(val id: String, val planName: String, val status: String)
data class VerifyPaymentRequest(val subscriptionId: String, val paymentId: String, val provider: String)
