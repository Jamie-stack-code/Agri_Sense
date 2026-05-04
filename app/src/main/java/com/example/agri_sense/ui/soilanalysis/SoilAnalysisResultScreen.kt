package com.example.agri_sense.ui.soilanalysis

import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.ml.SoilClassifier
import com.example.agri_sense.ui.dashboard.BottomNavBar
import com.example.agri_sense.ui.theme.*

@Composable
fun SoilAnalysisResultScreen(
    language: String,
    onNavigateToRecommendations: () -> Unit,
    onBackToHome: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val context = LocalContext.current
    val classifier = remember { SoilClassifier(context) }
    val isEnglish = language == "English"
    
    var isLoading by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf<SoilClassifier.SoilResult?>(null) }
    var qualityScore by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val mockDry = Uri.parse("content://dry")
        val mockWet = Uri.parse("content://wet")
        val analysis = classifier.analyzeSoil(mockDry, mockWet)
        result = analysis
        qualityScore = classifier.calculateQualityScore(analysis)
        isLoading = false
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "soil",
                onNavigateToHome = onBackToHome,
                onNavigateToSoil = {},
                onNavigateToMarket = onNavigateToMarket,
                onNavigateToCommunity = onNavigateToCommunity,
                onNavigateToProfile = onNavigateToProfile
            )
        },
        containerColor = SurfaceLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = if (isEnglish) "Analysis Results" else "Zotsatira za Kuyeza",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = PremiumDarkGreen
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(modifier = Modifier.height(400.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = PremiumGold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (isEnglish) "AI is analyzing soil texture..." else "AI ikuyeza nthaka yanu...",
                            color = OnSurfaceSubtle
                        )
                    }
                }
            } else {
                val analysis = result!!
                
                // PREMIUM SCORE GAUGE
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                    val animatedScore by animateFloatAsState(
                        targetValue = qualityScore.toFloat(),
                        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
                        label = "score"
                    )
                    
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            startAngle = 135f,
                            sweepAngle = 270f,
                            useCenter = false,
                            style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = PremiumGold,
                            startAngle = 135f,
                            sweepAngle = 270f * (animatedScore / 100f),
                            useCenter = false,
                            style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${animatedScore.toInt()}%",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = PremiumDarkGreen
                        )
                        Text(
                            text = if (isEnglish) "Quality" else "Ubwino",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceSubtle
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // SOIL TYPE CARD
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = analysis.type,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = PremiumDarkGreen
                            )
                        }
                        Text(
                            text = if (isEnglish) "Soil Texture Identified" else "Nthaka Yazindikirika",
                            color = OnSurfaceSubtle,
                            fontSize = 14.sp
                        )
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), color = Color(0xFFF5F5F5))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            ResultStat(label = "pH Level", value = analysis.properties.pH.toString(), color = PremiumGold)
                            ResultStat(label = if (isEnglish) "Moisture" else "Chinyezi", value = analysis.properties.moisture, color = StepBlue)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // NUTRIENT CARDS
                Text(
                    text = if (isEnglish) "Nutrient Levels" else "Zinthu M'nthaka",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PremiumDarkGreen,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PremiumNutrientRow("Nitrogen (N)", analysis.properties.nitrogen, PremiumDarkGreen, 0.4f)
                    PremiumNutrientRow("Phosphorus (P)", analysis.properties.phosphorus, PremiumGold, 0.7f)
                    PremiumNutrientRow("Potassium (K)", analysis.properties.potassium, StepBlue, 0.9f)
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = onNavigateToRecommendations,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(64.dp)
                        .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = PremiumGold),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text(
                        text = if (isEnglish) "Get Crop Recommendations" else "Landirani Malangizo a Mbewu",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun ResultStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Black, fontSize = 20.sp, color = color)
        Text(text = label, color = OnSurfaceSubtle, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PremiumNutrientRow(label: String, value: String, color: Color, progress: Float) {
    var animProgress by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        animProgress = progress
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = animProgress,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "nutrient"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = label, fontWeight = FontWeight.Bold, color = PremiumDarkGreen)
                Text(text = value, fontWeight = FontWeight.ExtraBold, color = color)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(8.dp).background(Color(0xFFEEEEEE), CircleShape)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(animatedProgress).height(8.dp).background(color, CircleShape)
                )
            }
        }
    }
}
