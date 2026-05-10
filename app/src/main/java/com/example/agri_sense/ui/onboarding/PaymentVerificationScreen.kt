package com.example.agri_sense.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.TextStyle
import com.example.agri_sense.ui.theme.*

@Composable
fun PaymentVerificationScreen(
    onPaymentSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var pin by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val isPremium by viewModel.isPremium.collectAsState()

    LaunchedEffect(isPremium) {
        if (isPremium) {
            onPaymentSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Premium Activation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = PremiumDarkGreen
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Simulating Airtel/TNM Money", color = Color.Gray, fontSize = 12.sp)
                Text("Amount: MK 5,000", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text("Enter Mobile Money PIN", fontSize = 14.sp)
                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    modifier = Modifier.width(150.dp),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center, 
                        letterSpacing = 8.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = PremiumDarkGreen,
                        unfocusedBorderColor = Color.Gray
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { viewModel.verifyPayment("sub_mock_123", "tx_mock_456", "AIRTEL") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = PremiumGold),
            colors = ButtonDefaults.buttonColors(
                containerColor = PremiumGold, 
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(28.dp),
            enabled = !loading && pin.length == 4
        ) {
            if (loading) {
                CircularProgressIndicator(color = PremiumDarkGreen, modifier = Modifier.size(24.dp))
            } else {
                Text("Pay MK 5,000", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
