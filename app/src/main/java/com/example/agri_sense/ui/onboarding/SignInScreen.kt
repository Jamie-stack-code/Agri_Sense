package com.example.agri_sense.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.agri_sense.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var countryCode by remember { mutableStateOf("+265") }
    var expandedCountry by remember { mutableStateOf(false) }
    var isVerifyingSubscription by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetStep by remember { mutableIntStateOf(1) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val authViewModel: AuthViewModel = hiltViewModel()

    LaunchedEffect(isVerifyingSubscription) {
        if (isVerifyingSubscription) {
            delay(1500) // Simulate network verification
            // Update phone number in the saved farmer record
            authViewModel.updatePhone("$countryCode$phone")
            isVerifyingSubscription = false
            onSignInSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PremiumDarkGreen, AgriGreenScreenBg)
                    ),
                    shape = RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.size(80.dp),
                    shadowElevation = 16.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = "Agri-Sense Logo",
                            tint = PremiumDarkGreen,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Access your premium insights",
                    color = PremiumGold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Form Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-40).dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                
                // Phone Number
                PremiumFieldLabel(text = "Phone Number", icon = Icons.Default.Phone)
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box {
                        Surface(
                            modifier = Modifier
                                .height(56.dp)
                                .clickable { expandedCountry = true },
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceLight,
                            border = BorderStroke(1.dp, Color.LightGray)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(countryCode, fontWeight = FontWeight.Bold, color = PremiumDarkGreen)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = PremiumDarkGreen)
                            }
                        }
                        DropdownMenu(
                            expanded = expandedCountry,
                            onDismissRequest = { expandedCountry = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(text = { Text("+265 (MW)") }, onClick = { countryCode = "+265"; expandedCountry = false })
                            DropdownMenuItem(text = { Text("+254 (KE)") }, onClick = { countryCode = "+254"; expandedCountry = false })
                            DropdownMenuItem(text = { Text("+260 (ZM)") }, onClick = { countryCode = "+260"; expandedCountry = false })
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("e.g. 991234567", color = OnSurfaceSubtle) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = premiumTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Password
                PremiumFieldLabel(text = "Password", icon = Icons.Default.Lock)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Enter password", color = OnSurfaceSubtle) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = premiumTextFieldColors(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null, tint = PremiumDarkGreen)
                        }
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Forgot Password?",
                    color = PremiumDarkGreen,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.End).clickable { showForgotPasswordDialog = true }
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { isVerifyingSubscription = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = PremiumDarkGreen),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen, disabledContainerColor = Color.LightGray),
                    shape = RoundedCornerShape(28.dp),
                    enabled = phone.isNotBlank() && password.isNotBlank() && !isVerifyingSubscription
                ) {
                    if (isVerifyingSubscription) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Verifying Access...", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Text(
                            text = "Sign In",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Don't have an account? ", color = OnSurfaceSubtle)
                    Text(
                        text = "Sign Up", 
                        color = PremiumDarkGreen, 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToSignUp() }
                    )
                }
            }
        }
    }

    if (showForgotPasswordDialog) {
        
        AlertDialog(
            onDismissRequest = {
                showForgotPasswordDialog = false
                resetStep = 1 // Reset steps when dismissed
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text("Reset Password", fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
            },
            text = {
                Column {
                    when (resetStep) {
                        1 -> {
                            Text("Enter your registered phone number. We will send an OTP.", color = OnSurfaceSubtle, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = "",
                                onValueChange = {},
                                placeholder = { Text("Phone Number") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }
                        2 -> {
                            Text("Enter the 4-digit code sent to your phone.", color = OnSurfaceSubtle, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = "",
                                onValueChange = {},
                                placeholder = { Text("0 0 0 0") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }
                        else -> {
                            Text("Enter your new password.", color = OnSurfaceSubtle, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = "",
                                onValueChange = {},
                                placeholder = { Text("New Password") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { 
                        if (resetStep < 3) resetStep++ 
                        else showForgotPasswordDialog = false 
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen)
                ) {
                    Text(if (resetStep == 3) "Finish Reset" else "Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Cancel", color = OnSurfaceSubtle)
                }
            }
        )
    }
}
