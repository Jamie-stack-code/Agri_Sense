package com.example.agri_sense.ui.soilanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.ui.theme.*
import com.example.agri_sense.ui.dashboard.BottomNavBar

val StepOrange = Color(0xFFFFA726)
val StepBlue = Color(0xFF039BE5)
val StepGreen = Color(0xFF43A047)

@Composable
fun SoilCameraScreen(
    language: String,
    onAnalysisComplete: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSoil: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val isEnglish = language == "English"
    
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "soil",
                onNavigateToHome = onNavigateToHome,
                onNavigateToSoil = {},
                onNavigateToMarket = onNavigateToMarket,
                onNavigateToCommunity = onNavigateToCommunity,
                onNavigateToProfile = onNavigateToProfile
            )
        },
        containerColor = PremiumSurface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PremiumDarkGreen, AgriGreenScreenBg)
                        ),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
                    .padding(top = 48.dp, bottom = 48.dp, start = 24.dp, end = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = CircleShape,
                        modifier = Modifier.size(88.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🍂", fontSize = 44.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = if (isEnglish) "AI Soil Analysis" else "Kuyeza Nthaka kwa AI",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isEnglish) 
                            "Capture the true state of your farm.\nTake 2 photos to unlock personalized insights." 
                        else 
                            "Jambulani zithunzi 2 za nthaka yanu kuti\nmulandire uphungu wa akatswiri.",
                        color = PremiumGold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Steps Section
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-24).dp)
                    .padding(horizontal = 24.dp)
            ) {
                // Step Cards
                PremiumAnalysisStepItem(
                    number = "1",
                    iconColor = StepOrange,
                    title = if (isEnglish) "Dry Soil Sample" else "Nthaka Youma",
                    subtitle = if (isEnglish) "Take a clear photo of your soil in natural light." else "Jambulani bwino chithunzi cha nthaka youma."
                )
                Spacer(modifier = Modifier.height(16.dp))
                PremiumAnalysisStepItem(
                    number = "2",
                    iconColor = StepBlue,
                    title = if (isEnglish) "Wet Soil Sample" else "Nthaka Yonyowa",
                    subtitle = if (isEnglish) "Add a little water to the soil and photograph it again." else "Thirani madzi panthaka ndiye jambulaninso."
                )
                Spacer(modifier = Modifier.height(16.dp))
                PremiumAnalysisStepItem(
                    number = "3",
                    iconColor = StepGreen,
                    title = if (isEnglish) "AI Processing" else "Malangizo a AI",
                    subtitle = if (isEnglish) "Our AI analyzes texture and color to suggest the perfect crops." else "AI iyeza nthaka ndikukupatsani malangizo a mbewu."
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Premium Start Button
                Button(
                    onClick = onAnalysisComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = PremiumGold),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = PremiumGold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isEnglish) "Launch Camera" else "Yambani Kuyeza",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun PremiumAnalysisStepItem(
    number: String,
    iconColor: Color,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = iconColor.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = number,
                        color = iconColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PremiumDarkGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = OnSurfaceSubtle,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
