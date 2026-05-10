package com.example.agri_sense.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.agri_sense.ui.theme.*

@Composable
fun PlanSelectionScreen(
    onPlanSelected: (Boolean) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose Your Plan",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = PremiumDarkGreen,
            modifier = Modifier.padding(top = 48.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        PlanCard(
            title = "Free Trial",
            price = "MK 0",
            features = listOf("Basic Soil Analysis", "Weather Alerts", "Community Access"),
            isPremium = false,
            onClick = { 
                viewModel.selectPlan("FREE")
                onPlanSelected(false) 
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        PlanCard(
            title = "Premium Authority",
            price = "MK 5,000/mo",
            features = listOf("Advanced Neural Insights", "Direct Expert Chat", "Market Intelligence", "Disease Validation"),
            isPremium = true,
            onClick = { 
                viewModel.selectPlan("PREMIUM")
                onPlanSelected(true) 
            }
        )
    }
}

@Composable
fun PlanCard(
    title: String,
    price: String,
    features: List<String>,
    isPremium: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isPremium) 16.dp else 4.dp, RoundedCornerShape(24.dp), spotColor = if (isPremium) PremiumGold else Color.Gray)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = if (isPremium) PremiumDarkGreen else Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPremium) PremiumGold else PremiumDarkGreen
                )
                if (isPremium) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Filled.Star, contentDescription = null, tint = PremiumGold)
                }
            }
            Text(
                text = price,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = if (isPremium) Color.White else Color.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            features.forEach { feature ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Check, 
                        contentDescription = null, 
                        tint = if (isPremium) PremiumGold else PremiumDarkGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature, 
                        color = if (isPremium) Color.White.copy(alpha = 0.8f) else Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPremium) PremiumGold else PremiumDarkGreen,
                    contentColor = if (isPremium) PremiumDarkGreen else Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Select Plan", fontWeight = FontWeight.Bold)
            }
        }
    }
}
