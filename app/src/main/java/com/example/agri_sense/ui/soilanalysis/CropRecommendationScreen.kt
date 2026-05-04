package com.example.agri_sense.ui.soilanalysis

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.data.models.CropRecommendation
import com.example.agri_sense.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropRecommendationScreen(
    language: String,
    onBackToHome: () -> Unit
) {
    val isEnglish = language == "English"
    var isLoading by remember { mutableStateOf(true) }
    var recommendations by remember { mutableStateOf<List<CropRecommendation>>(emptyList()) }

    LaunchedEffect(Unit) {
        delay(1500)
        recommendations = listOf(
            CropRecommendation(
                soilAnalysisId = 0,
                cropName = "Maize (Hybrid SC719)",
                cropNameChichewa = "Chimanga (SC719)",
                confidenceScore = 0.92f,
                reasonSummary = "Your soil has excellent loam texture and near-neutral pH — ideal for high-yielding maize.",
                plantingGuide = "Plant early after first rains. Space 75cm between rows, 25cm between plants. Target population: 53,000 plants/ha.",
                fertilizerAdvice = "Apply 2 bags NPK 23-21-0+4S per acre at planting, then 2 bags Urea/CAN at knee height (V4).",
                wateringNeeds = "Needs 500-800mm/season. Critical at tasseling and grain fill — do not allow drought then."
            ),
            CropRecommendation(
                soilAnalysisId = 0,
                cropName = "Soybeans (Tikolore)",
                cropNameChichewa = "Soya (Tikolore)",
                confidenceScore = 0.85f,
                reasonSummary = "Soil pH and texture suit nitrogen-fixing legumes. High demand and excellent prices in Malawi.",
                plantingGuide = "Plant in mid-December. Space 45cm x 5cm. Inoculate with Optimize before planting.",
                fertilizerAdvice = "Apply SSP 200kg/ha at planting. No nitrogen needed if properly inoculated.",
                wateringNeeds = "Needs 400-800mm. Critical at podding stage."
            )
        )
        isLoading = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        if (isEnglish) "AI Recommendations" else "Malangizo a AI",
                        fontWeight = FontWeight.Bold,
                        color = PremiumDarkGreen
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PremiumDarkGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PremiumSurface)
            )
        },
        containerColor = PremiumSurface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text(
                text = if (isEnglish) "Best Crops for Your Soil" else "Mbewu Zabwino Nthaka Yanu",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = PremiumDarkGreen
            )
            Text(
                text = if (isEnglish) "Tap a card to view planting guides" else "Gwirani khadi kuti muwone malangizo",
                color = OnSurfaceSubtle,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PremiumGold)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(recommendations) { crop ->
                        PremiumInteractiveCropCard(crop, isEnglish)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = PremiumGold),
                colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                shape = RoundedCornerShape(32.dp)
            ) {
                Text(
                    text = if (isEnglish) "Return to Home" else "Bwererani Kunyumba", 
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun PremiumInteractiveCropCard(crop: CropRecommendation, isEnglish: Boolean) {
    var flipped by remember { mutableStateOf(false) }
    
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "crop_flip"
    )
    
    val animateFront by derivedStateOf { rotation <= 90f }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { flipped = !flipped },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = if (animateFront) Color.White else PremiumDarkGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        if (animateFront) {
            // FRONT
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    color = PremiumDarkGreen.copy(alpha = 0.05f),
                    shape = CircleShape,
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (crop.cropName.contains("Maize")) "🌽" else "🌱",
                            fontSize = 40.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = crop.cropName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PremiumDarkGreen,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${(crop.confidenceScore * 100).toInt()}% ${if (isEnglish) "Match" else "Mgwirizano"}",
                        color = PremiumGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = if (isEnglish) "Tap to reveal guide" else "Gwirani kuti muwone malangizo",
                    color = OnSurfaceSubtle,
                    fontSize = 12.sp
                )
            }
        } else {
            // BACK (Flipped)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
                    .padding(24.dp),
            ) {
                Text(
                    text = if (isEnglish) "Planting Guide" else "Malangizo Okadzala",
                    color = PremiumGold,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = crop.plantingGuide,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = if (isEnglish) "Fertilizer Management" else "Kasamalidwe ka Manyowa",
                    color = PremiumGold,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = crop.fertilizerAdvice,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Voice / Listen button inside the flipped card
                Button(
                    onClick = { /* TODO: Play audio for advice */ },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumGold, contentColor = PremiumDarkGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (isEnglish) "Listen to Advice" else "Mverani Malangizo", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
