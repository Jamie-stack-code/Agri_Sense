package com.example.agri_sense.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.agri_sense.ui.components.AgriSenseLogo
import com.example.agri_sense.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onOtpSent: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
    language: String = "English"
) {
    var phone by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val otpDispatched by viewModel.otpDispatched.collectAsState()

    LaunchedEffect(otpDispatched) {
        if (otpDispatched != null) {
            onOtpSent(phone)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (language == "English") "Reset Password" else "Sinthani Chinsinsi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceLight)
            )
        },
        containerColor = SurfaceLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Surface(
                color = PremiumDarkGreen.copy(alpha = 0.05f),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.size(100.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Phone, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(48.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (language == "English") "Forgot your password?" else "Mwayiwala chinsinsi?",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PremiumDarkGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (language == "English") "Enter your phone number to receive a 6-digit reset code via SMS." else "Lembani nambala yanu ya foni kuti mulandire kodi yosinthira chinsinsi.",
                color = OnSurfaceSubtle,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(if (language == "English") "Phone Number" else "Nambala ya Foni") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null, tint = PremiumDarkGreen) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = PremiumDarkGreen,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.requestPhoneOtp(phone) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = PremiumDarkGreen),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PremiumDarkGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                enabled = !loading && phone.isNotBlank()
            ) {
                if (loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = if (language == "English") "Send Reset Code" else "Tumizani Kodi",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
