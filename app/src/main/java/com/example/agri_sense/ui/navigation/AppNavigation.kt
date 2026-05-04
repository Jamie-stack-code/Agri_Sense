package com.example.agri_sense.ui.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agri_sense.ui.community.CommunityScreen
import com.example.agri_sense.ui.community.PestDiagnosisScreen
import com.example.agri_sense.ui.dashboard.HomeScreen
import com.example.agri_sense.ui.market.MarketScreen
import com.example.agri_sense.ui.market.NearbyMarketsScreen
import com.example.agri_sense.ui.onboarding.AuthViewModel
import com.example.agri_sense.ui.onboarding.LanguageScreen
import com.example.agri_sense.ui.onboarding.ProfileSetupScreen
import com.example.agri_sense.ui.onboarding.SignInScreen
import com.example.agri_sense.ui.onboarding.SignUpScreen
import com.example.agri_sense.ui.onboarding.SubscriptionPaymentScreen
import com.example.agri_sense.ui.onboarding.WelcomeScreen
import com.example.agri_sense.ui.profile.FarmerProfileScreen
import com.example.agri_sense.ui.profile.ManageProfileScreen
import com.example.agri_sense.ui.profile.SettingsScreen
import com.example.agri_sense.ui.soilanalysis.CropRecommendationScreen
import com.example.agri_sense.ui.soilanalysis.SoilAnalysisResultScreen
import com.example.agri_sense.ui.soilanalysis.SoilCameraScreen
import com.example.agri_sense.ui.soilanalysis.SoilCaptureScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Shared AuthViewModel scoped to the nav graph
    val authViewModel: AuthViewModel = hiltViewModel()
    val isOnboarded by authViewModel.isOnboarded.collectAsState()

    // Language state — sourced from authViewModel / DataStore in future iteration
    var selectedLanguage by remember { mutableStateOf("English") }

    // Show nothing while checking onboarding status (null = loading)
    if (isOnboarded == null) return

    val startDestination = if (isOnboarded == true) "home" else "welcome"

    NavHost(navController = navController, startDestination = startDestination) {

        // ── Onboarding ──────────────────────────────────────────────────────────
        composable("welcome") {
            WelcomeScreen(onNavigateForward = {
                navController.navigate("language_selection")
            })
        }

        composable("language_selection") {
            LanguageScreen(onLanguageSelected = { lang ->
                selectedLanguage = lang
                navController.navigate("profile_setup")
            })
        }

        composable("profile_setup") {
            ProfileSetupScreen(
                onSetupFree = { navController.navigate("sign_in?isPremium=false") },
                onSetupPremium = { navController.navigate("sign_in?isPremium=true") }
            )
        }

        composable(
            "sign_up?isPremium={isPremium}",
            arguments = listOf(navArgument("isPremium") { defaultValue = false })
        ) { backStackEntry ->
            val isPremium = backStackEntry.arguments?.getBoolean("isPremium") ?: false
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("sign_in?isPremium=$isPremium") {
                        popUpTo("profile_setup") { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.navigate("sign_in?isPremium=$isPremium")
                }
            )
        }

        composable(
            "sign_in?isPremium={isPremium}",
            arguments = listOf(navArgument("isPremium") { defaultValue = false })
        ) { backStackEntry ->
            val isPremium = backStackEntry.arguments?.getBoolean("isPremium") ?: false
            SignInScreen(
                onSignInSuccess = {
                    // Mark as onboarded via ViewModel
                    if (isPremium) {
                        authViewModel.activatePremium()
                        navController.navigate("payment") {
                            popUpTo("sign_in") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("sign_up?isPremium=$isPremium")
                }
            )
        }

        composable("payment") {
            SubscriptionPaymentScreen(
                onPaymentSuccess = {
                    authViewModel.activatePremium()
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Main Screens ─────────────────────────────────────────────────────────
        composable("home") {
            HomeScreen(
                language = selectedLanguage,
                onToggleLanguage = {
                    selectedLanguage = if (selectedLanguage == "English") "Chichewa" else "English"
                },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToSoil = { navController.navigate("soil_camera") },
                onNavigateToMarket = { navController.navigate("market") },
                onNavigateToCommunity = { navController.navigate("community") },
                onNavigateToRecommendations = { navController.navigate("crop_recommendations") }
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
                onLogout = {
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
                onPhotosCaptured = { navController.navigate("soil_result") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("soil_result") {
            SoilAnalysisResultScreen(
                language = selectedLanguage,
                onNavigateToRecommendations = { navController.navigate("crop_recommendations") },
                onBackToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateToMarket = { navController.navigate("market") },
                onNavigateToCommunity = { navController.navigate("community") },
                onNavigateToProfile = { navController.navigate("profile") }
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
