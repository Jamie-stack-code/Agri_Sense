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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.ui.theme.*
import com.example.agri_sense.ui.components.AgriSenseLogo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var countryCode by remember { mutableStateOf("+265") }
    var expandedCountry by remember { mutableStateOf(false) }
    var showOtpStep by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var isVerifyingOtp by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val generatedOtp = remember { "4829" } // Static for demo purposes
    
    val hasMinLength = password.length >= 8
    val hasUppercase = password.any { it.isUpperCase() }
    val hasNumber = password.any { it.isDigit() }
    val isPasswordStrong = hasMinLength && hasUppercase && hasNumber

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    AgriSenseLogo(
                        size = 48.dp,
                        tint = PremiumDarkGreen
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Join the future of farming",
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
                if (showOtpStep) {
                    // --- OTP Verification Step ---
                    Text(
                        text = "Verify Your Phone",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PremiumDarkGreen,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "We've sent a 4-digit code to $countryCode$phone via SMS.",
                        fontSize = 14.sp,
                        color = OnSurfaceSubtle,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { if (it.length <= 4) otpCode = it },
                        placeholder = { Text("0 0 0 0", color = OnSurfaceSubtle, fontSize = 24.sp, letterSpacing = 8.sp, textAlign = TextAlign.Center) },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = premiumTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 24.sp, letterSpacing = 8.sp, fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            isVerifyingOtp = true
                            coroutineScope.launch {
                                kotlinx.coroutines.delay(1500) // Simulate network/SMS verification
                                isVerifyingOtp = false
                                onSignUpSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = PremiumDarkGreen),
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen, disabledContainerColor = Color.LightGray),
                        shape = RoundedCornerShape(28.dp),
                        enabled = otpCode.length == 4 && otpCode == generatedOtp && !isVerifyingOtp
                    ) {
                        if (isVerifyingOtp) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Verifying...", color = Color.White, fontWeight = FontWeight.Bold)
                        } else {
                            Text("Confirm & Create Account", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Resend Code",
                        color = PremiumGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally).clickable { otpCode = "" }
                    )

                } else {
                    // --- Standard Registration Step ---
                    // Phone Number
                PremiumFieldLabel(text = "Phone Number", icon = Icons.Default.Phone)
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    // Country Code Selector
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
                    placeholder = { Text("Create password", color = OnSurfaceSubtle) },
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
                
                // Password Strength Indicators
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (hasMinLength) Icons.Default.CheckCircle else Icons.Default.Cancel, contentDescription = null, tint = if (hasMinLength) PremiumDarkGreen else Color.LightGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("At least 8 characters", fontSize = 12.sp, color = if (hasMinLength) PremiumDarkGreen else Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (hasUppercase) Icons.Default.CheckCircle else Icons.Default.Cancel, contentDescription = null, tint = if (hasUppercase) PremiumDarkGreen else Color.LightGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("One uppercase letter", fontSize = 12.sp, color = if (hasUppercase) PremiumDarkGreen else Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (hasNumber) Icons.Default.CheckCircle else Icons.Default.Cancel, contentDescription = null, tint = if (hasNumber) PremiumDarkGreen else Color.LightGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("One number", fontSize = 12.sp, color = if (hasNumber) PremiumDarkGreen else Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Password
                PremiumFieldLabel(text = "Confirm Password", icon = Icons.Default.Lock)
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Confirm password", color = OnSurfaceSubtle) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = premiumTextFieldColors(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { 
                        showOtpStep = true 
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(1000)
                            snackbarHostState.showSnackbar("📨 New SMS: Your Agri-Sense code is $generatedOtp")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = PremiumDarkGreen),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen, disabledContainerColor = Color.LightGray),
                    shape = RoundedCornerShape(28.dp),
                    enabled = phone.isNotBlank() && isPasswordStrong && password == confirmPassword
                ) {
                    Text(
                        text = "Continue via SMS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Already have an account? ", color = OnSurfaceSubtle)
                    Text(
                        text = "Sign In", 
                        color = PremiumDarkGreen, 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToSignIn() }
                    )
                }
                }
                }
            }
        }
    }
}
