package com.example.agri_sense.ui.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.ui.components.AgriSenseLogo
import com.example.agri_sense.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationComplete: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val opacity = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        opacity.animateTo(1f, tween(1000))
        delay(2000)
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PremiumForest, PremiumDarkGreen)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
                    .clip(CircleShape)
                    .background(PremiumGold.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                AgriSenseLogo(size = 80.dp, tint = PremiumGold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "AGRI-SENSE",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp,
                modifier = Modifier.scale(scale.value)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "AFRICA'S POCKET FARM ADVISOR",
                color = PremiumGold.copy(alpha = opacity.value),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }
        
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .size(24.dp),
            color = PremiumGold,
            strokeWidth = 2.dp
        )
    }
}
