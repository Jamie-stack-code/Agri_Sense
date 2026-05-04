package com.example.agri_sense.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.ui.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionPaymentScreen(
    onPaymentSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    var selectedMethod by remember { mutableStateOf("Airtel") }
    var isProcessing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }

    val amountUSD = 3.00
    val amountMWK = 5200

    if (isSuccess) {
        // Success Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PremiumDarkGreen)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = PremiumGold,
                shape = CircleShape,
                modifier = Modifier.size(100.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.padding(20.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Payment Successful!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Your Premium Season Pass is now active.", fontSize = 16.sp, color = PremiumGold, textAlign = TextAlign.Center)
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = onPaymentSuccess,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PremiumGold),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Start Farming", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PremiumDarkGreen)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Premium Subscription", fontWeight = FontWeight.Bold, color = PremiumDarkGreen) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PremiumDarkGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceLight)
            )
        },
        containerColor = SurfaceLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Price Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PremiumDarkGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("FULL SEASON PASS", color = PremiumGold, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("MK $amountMWK", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp)
                    Text("~$amountUSD USD (Auto-converted)", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Unlimited AI Soil Analysis", color = Color.White, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Real-time Market Prices", color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Select Payment Method", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PremiumDarkGreen, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(16.dp))

            // Payment Methods
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PaymentMethodCard(
                    title = "Airtel Money",
                    isSelected = selectedMethod == "Airtel",
                    onClick = { selectedMethod = "Airtel" },
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE50000) // Airtel Red
                )
                PaymentMethodCard(
                    title = "TNM Mpamba",
                    isSelected = selectedMethod == "TNM",
                    onClick = { selectedMethod = "TNM" },
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF006633) // TNM Green
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Phone Number Input
            PremiumFieldLabel(text = "Mobile Money Number", icon = Icons.Default.PhoneIphone)
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = { Text("e.g. 099... or 088...", color = OnSurfaceSubtle) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = premiumTextFieldColors(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Pay Button
            Button(
                onClick = {
                    if (phoneNumber.isNotBlank()) {
                        isProcessing = true
                        coroutineScope.launch {
                            delay(2500) // Simulate mobile money processing
                            authViewModel.activatePremium() // Persist premium to Room
                            isProcessing = false
                            isSuccess = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = PremiumDarkGreen),
                colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                shape = RoundedCornerShape(32.dp),
                enabled = !isProcessing && phoneNumber.isNotBlank()
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(color = PremiumGold, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Processing...", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PremiumGold)
                } else {
                    Text("Pay MK $amountMWK Now", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PremiumGold)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PaymentMethodCard(title: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, color: Color) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) color.copy(alpha = 0.1f) else Color.White),
        border = BorderStroke(2.dp, if (isSelected) color else Color.LightGray.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(title, fontWeight = FontWeight.Bold, color = if (isSelected) color else Color.DarkGray, fontSize = 16.sp)
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle, 
                    contentDescription = null, 
                    tint = color, 
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(16.dp)
                )
            }
        }
    }
}
