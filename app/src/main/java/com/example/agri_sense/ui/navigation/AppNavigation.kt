package com.example.agri_sense.ui.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agri_sense.ui.community.CommunityScreen
import com.example.agri_sense.ui.community.PestDiagnosisScreen
import com.example.agri_sense.ui.dashboard.AdvisoryScreen
import com.example.agri_sense.ui.dashboard.HomeScreen
import com.example.agri_sense.ui.dashboard.WeatherForecastScreen
import com.example.agri_sense.ui.market.MarketScreen
import com.example.agri_sense.ui.market.NearbyMarketsScreen
import com.example.agri_sense.ui.onboarding.AuthViewModel
import com.example.agri_sense.ui.onboarding.LanguageScreen
import com.example.agri_sense.ui.onboarding.SignInScreen
import com.example.agri_sense.ui.onboarding.SignUpScreen
import com.example.agri_sense.ui.onboarding.OtpVerificationScreen
import com.example.agri_sense.ui.onboarding.PlanSelectionScreen
import com.example.agri_sense.ui.onboarding.PaymentVerificationScreen
import com.example.agri_sense.ui.onboarding.ForgotPasswordScreen
import com.example.agri_sense.ui.onboarding.ResetPasswordScreen
import com.example.agri_sense.ui.onboarding.SplashScreen
import com.example.agri_sense.ui.onboarding.WelcomeScreen
import com.example.agri_sense.ui.profile.FarmerProfileScreen
import com.example.agri_sense.ui.profile.ManageProfileScreen
import com.example.agri_sense.ui.profile.SettingsScreen
import com.example.agri_sense.ui.soilanalysis.CropRecommendationScreen
import com.example.agri_sense.ui.soilanalysis.SoilAnalysisResultScreen
import com.example.agri_sense.ui.soilanalysis.SoilCameraScreen
import com.example.agri_sense.ui.soilanalysis.SoilCaptureScreen
import com.example.agri_sense.ui.soilanalysis.SoilHistoryScreen
import com.example.agri_sense.ui.soilanalysis.SoilViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Shared AuthViewModel scoped to the nav graph
    val authViewModel: AuthViewModel = hiltViewModel()
    val isOnboarded by authViewModel.isOnboarded.collectAsState()
    val isProfileComplete by authViewModel.isProfileComplete.collectAsState()

    // SoilViewModel scoped to AppNavigation so SoilCaptureScreen and
    // SoilAnalysisResultScreen share the same ViewModel instance
    val soilViewModel: SoilViewModel = hiltViewModel()

    val settingsViewModel: com.example.agri_sense.ui.profile.SettingsViewModel = hiltViewModel()
    val selectedLanguage by settingsViewModel.language.collectAsState()

    // Show nothing while checking onboarding status (null = loading)
    if (isOnboarded == null) return

    CompositionLocalProvider(com.example.agri_sense.ui.theme.LocalAppLanguage provides selectedLanguage) {
        NavHost(navController = navController, startDestination = "splash") {

            composable("splash") {
                SplashScreen(onAnimationComplete = {
                    val destination = if (isOnboarded == true) {
                        if (isProfileComplete == true) "home" else "profile_setup"
                    } else "welcome"
                    navController.navigate(destination) {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }

            // ── Onboarding ──────────────────────────────────────────────────────────
            composable("welcome") {
                WelcomeScreen(onNavigateForward = {
                    navController.navigate("language_selection")
                })
            }

            composable("language_selection") {
                LanguageScreen(onLanguageSelected = { lang ->
                    settingsViewModel.setLanguage(lang)
                    navController.navigate("sign_up")
                })
            }

            composable("sign_up") {
            SignUpScreen(
                language = selectedLanguage,
                onSignUpSuccess = { phone ->
                    navController.navigate("otp_verification?phone=$phone")
                },
                onNavigateToSignIn = { navController.navigate("sign_in") }
            )
        }

        composable("sign_in") {
            SignInScreen(
                onSignInSuccess = { isComplete ->
                    if (isComplete) {
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    } else {
                        navController.navigate("profile_setup") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                },
                onNavigateToSignUp = { navController.navigate("sign_up") },
                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                language = selectedLanguage
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onOtpSent = { phone ->
                    navController.navigate("reset_password?phone=$phone")
                },
                onBack = { navController.popBackStack() },
                language = selectedLanguage
            )
        }

        composable(
            "reset_password?phone={phone}",
            arguments = listOf(navArgument("phone") { defaultValue = "" })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            ResetPasswordScreen(
                phone = phone,
                onResetSuccess = {
                    navController.navigate("sign_in") {
                        popUpTo("sign_in") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
                language = selectedLanguage
            )
        }

        composable(
            "otp_verification?phone={phone}",
            arguments = listOf(navArgument("phone") { defaultValue = "" })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpVerificationScreen(
                phone = phone,
                onVerifySuccess = {
                    navController.navigate("plan_selection")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("plan_selection") {
            PlanSelectionScreen(
                onPlanSelected = { isPremium ->
                    if (isPremium) {
                        navController.navigate("payment")
                    } else {
                        navController.navigate("profile_setup") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("payment") {
            PaymentVerificationScreen(
                onPaymentSuccess = {
                    navController.navigate("profile_setup") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        composable("profile_setup") {
            com.example.agri_sense.ui.onboarding.ProfileSetupScreen(
                onSetupComplete = {
                    navController.navigate("home") {
                        popUpTo("profile_setup") { inclusive = true }
                    }
                }
            )
        }

        // ── Main Screens ─────────────────────────────────────────────────────────
        composable("home") {
            HomeScreen(
                language = selectedLanguage,
                onToggleLanguage = {
                    settingsViewModel.setLanguage(if (selectedLanguage == "English") "Chichewa" else "English")
                },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToSoil = { navController.navigate("soil_camera") },
                onNavigateToMarket = { navController.navigate("market") },
                onNavigateToCommunity = { navController.navigate("community") },
                onNavigateToWeather = { navController.navigate("weather_details") }
            )
        }

        composable("advisory") {
            AdvisoryScreen(onBack = { navController.popBackStack() })
        }

        composable("weather_details") {
            WeatherForecastScreen(
                onBack = { navController.popBackStack() },
                language = selectedLanguage
            )
        }

        composable("market") {
            MarketScreen(
                language = selectedLanguage,
                onBack = { navController.popBackStack() },
                onNavigateToNearby = { navController.navigate("nearby_markets") },
                onNavigateToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateToSoil = { navController.navigate("soil_camera") },
                onNavigateToCommunity = { navController.navigate("community") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }

        composable("nearby_markets") {
            NearbyMarketsScreen(onBack = { navController.popBackStack() })
        }

        composable("community") {
            CommunityScreen(
                language = selectedLanguage,
                onBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateToSoil = { navController.navigate("soil_camera") },
                onNavigateToMarket = { navController.navigate("market") },
                onNavigateToPestDiagnosis = { navController.navigate("pest_diagnosis") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }

        composable("pest_diagnosis") {
            PestDiagnosisScreen(onBack = { navController.popBackStack() })
        }

        // ── Profile ───────────────────────────────────────────────────────────────
        composable("profile") {
            FarmerProfileScreen(
                onBack = { navController.popBackStack() },
                onNavigateToPayment = { navController.navigate("payment") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToEditProfile = { navController.navigate("manage_profile") },
                onNavigateToSoilHistory = { navController.navigate("soil_history") },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("welcome") { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable("manage_profile") {
            ManageProfileScreen(
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        // ── Soil Analysis ─────────────────────────────────────────────────────────
        composable("soil_camera") {
            SoilCameraScreen(
                language = selectedLanguage,
                onAnalysisComplete = { navController.navigate("soil_capture") },
                onNavigateToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateToSoil = {},
                onNavigateToMarket = { navController.navigate("market") },
                onNavigateToCommunity = { navController.navigate("community") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }

        composable("soil_capture") {
            SoilCaptureScreen(
                viewModel = soilViewModel,
                onPhotosCaptured = { navController.navigate("soil_result") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("soil_result") {
            SoilAnalysisResultScreen(
                viewModel = soilViewModel,
                language = selectedLanguage,
                onNavigateToRecommendations = { navController.navigate("soil_history") },
                onBackToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateToMarket = { navController.navigate("market") },
                onNavigateToCommunity = { navController.navigate("community") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }

        composable("soil_history") {
            SoilHistoryScreen(
                viewModel = soilViewModel,
                language = selectedLanguage,
                onBack = { navController.popBackStack() }
            )
        }

        composable("crop_recommendations") {
                CropRecommendationScreen(
                    language = selectedLanguage,
                    onBackToHome = {
                        navController.navigate("home") { popUpTo("home") { inclusive = true } }
                    }
                )
            }
        }
    }
}
