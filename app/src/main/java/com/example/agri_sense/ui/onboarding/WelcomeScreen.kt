package com.example.agri_sense.ui.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.agri_sense.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.agri_sense.R
import com.example.agri_sense.ui.components.AgriSenseLogo

@Composable
fun WelcomeScreen(onNavigateForward: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // PREMIUM HERO SECTION
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))
        ) {
            val images = listOf(
                R.drawable.img_grown_local,
                R.drawable.img_harvest_basket,
                R.drawable.img_farmer_tablet,
                R.drawable.img_farmer_produce
            )
            var currentImageIdx by remember { mutableIntStateOf(0) }
            
            LaunchedEffect(Unit) {
                while (true) {
                    delay(4000)
                    currentImageIdx = (currentImageIdx + 1) % images.size
                }
            }

            AnimatedContent(
                targetState = currentImageIdx,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(1500, easing = LinearEasing)) + 
                     scaleIn(initialScale = 1.05f, animationSpec = tween(1500, easing = LinearEasing))).togetherWith(
                     fadeOut(animationSpec = tween(1500, easing = LinearEasing)))
                },
                label = "hero_carousel"
            ) { targetIdx ->
                Image(
                    painter = painterResource(id = images[targetIdx]),
                    contentDescription = "Agri-Sense Hero Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Dark overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.7f),
                                PremiumDarkGreen.copy(alpha = 0.95f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // High Premium Hero Style Logo and Text
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp, bottom = 48.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // New Premium Text Logo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AgriSenseLogo(
                        size = 48.dp,
                        tint = PremiumGold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AGRI-SENSE",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 4.sp
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Surface(
                    color = PremiumGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, PremiumGold.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "AFRICA'S POCKET FARM ADVISOR",
                        color = PremiumGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Smart Advice.\nBetter Harvest.",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 38.sp
                )
            }
        }

        // MISSION (Glassmorphic overlapping card)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-40).dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Our Mission", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PremiumDarkGreen)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Empowering farmers with cutting-edge AI. Real-time insights to increase yields, improve soil health, and ensure ultimate food security.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            // STRATEGIC GOALS (Premium Row)
            Text(
                text = "Strategic Goals",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = PremiumDarkGreen
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PremiumGoalCard(title = "Zero\nHunger", icon = Icons.Filled.Restaurant, modifier = Modifier.weight(1f))
                PremiumGoalCard(title = "Max\nProsperity", icon = Icons.AutoMirrored.Filled.TrendingUp, modifier = Modifier.weight(1f))
                PremiumGoalCard(title = "True\nSustainability", icon = Icons.Filled.FilterVintage, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(48.dp))

            // CORE FEATURES (Interactive Flip Cards)
            Text(
                text = "Core Features",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = PremiumDarkGreen
            )
            Text(
                text = "Tap cards to reveal",
                fontSize = 14.sp,
                color = OnSurfaceSubtle
            )
            Spacer(modifier = Modifier.height(16.dp))

            InteractiveFlipFeature(
                title = "Soil Analysis",
                desc = "Precision nutrient tracking and pH balancing using advanced computer vision.",
                icon = Icons.Filled.Science
            )
            Spacer(modifier = Modifier.height(16.dp))
            InteractiveFlipFeature(
                title = "Rapid Pest Diagnosis",
                desc = "Detect over 50+ common African pests and diseases instantly with your camera.",
                icon = Icons.Filled.BugReport
            )
            Spacer(modifier = Modifier.height(16.dp))
            InteractiveFlipFeature(
                title = "Market Connectivity",
                desc = "Direct links to trusted wholesalers and real-time pricing data across districts.",
                icon = Icons.Filled.Hub
            )

            Spacer(modifier = Modifier.height(48.dp))

            // TESTIMONIALS (Perfect Auto-Flip)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Success Stories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = PremiumDarkGreen
                )
                
                // Live Update Badge
                Surface(
                    color = PremiumGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LIVE • Updated Today",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PremiumDarkGreen
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            PerfectFlipTestimonials()

            Spacer(modifier = Modifier.height(56.dp))

            // PREMIUM CTA
            Button(
                onClick = onNavigateForward,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = AgriGreenScreenBg),
                colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                shape = RoundedCornerShape(32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Get Started",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun PremiumGoalCard(title: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.aspectRatio(0.85f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = PremiumDarkGreen.copy(alpha = 0.05f),
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title, 
                fontSize = 13.sp, 
                fontWeight = FontWeight.Bold, 
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun InteractiveFlipFeature(title: String, desc: String, icon: ImageVector) {
    var flipped by remember { mutableStateOf(false) }
    
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "flip"
    )
    
    val animateFront by derivedStateOf { rotation <= 90f }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .graphicsLayer {
                rotationX = rotation
                cameraDistance = 12f * density
            }
            .clickable { flipped = !flipped },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (animateFront) Color.White else PremiumDarkGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        if (animateFront) {
            // FRONT
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = PremiumDarkGreen.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            }
        } else {
            // BACK (Flipped)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationX = 180f }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = desc,
                    color = Color.White,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
fun PerfectFlipTestimonials() {
    val testimonials = listOf(
        Testimonial("John Banda", "Lilongwe", "Agri-Sense helped me identify Fall Armyworm 2 weeks earlier. Saved 80% of my harvest!", R.drawable.img_john_banda, "🌽🌽🌽"),
        Testimonial("Mary Phiri", "Blantyre", "The soil analysis is magic. I used half the fertilizer and got my biggest harvest ever.", R.drawable.img_mary_phiri, "🌾🌾🌾"),
        Testimonial("Limbani Gondwe", "Mzuzu", "Checking market prices every morning helped me find buyers who pay 20% more for soy.", R.drawable.img_limbani_gondwe, "💰💰💰")
    )

    var currentIdx by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentIdx = (currentIdx + 1) % testimonials.size
        }
    }

    Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
        AnimatedContent(
            targetState = currentIdx,
            transitionSpec = {
                // Perfect Flip Transition
                (fadeIn(animationSpec = tween(400, delayMillis = 200)) +
                 scaleIn(initialScale = 0.9f, animationSpec = tween(400, delayMillis = 200))).togetherWith(
                 fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 0.9f, animationSpec = tween(400)))
            },
            label = "testimonial_flip"
        ) { index ->
            val testimonial = testimonials[index]
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = BorderStroke(1.dp, PremiumDarkGreen.copy(alpha = 0.1f))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Premium Quotation Mark Background Overlay
                    Icon(
                        imageVector = Icons.Filled.FormatQuote,
                        contentDescription = null,
                        tint = PremiumGold.copy(alpha = 0.05f),
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 20.dp, y = (-10).dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Circular Profile Picture
                            Image(
                                painter = painterResource(id = testimonial.imageRes),
                                contentDescription = "${testimonial.name} profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, PremiumGold, CircleShape)
                                    .background(Color.LightGray)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = testimonial.name, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen, fontSize = 20.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Farmer from ${testimonial.location}", fontSize = 14.sp, color = OnSurfaceSubtle)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "\"${testimonial.text}\"",
                            style = MaterialTheme.typography.bodyLarge.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                            color = Color.DarkGray,
                            lineHeight = 26.sp,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
    
    // Premium Progress Indicators
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        testimonials.indices.forEach { index ->
            val isSelected = index == currentIdx
            val width by animateDpAsState(targetValue = if (isSelected) 24.dp else 8.dp, label = "indicator")
            val color by animateColorAsState(targetValue = if (isSelected) PremiumDarkGreen else Color.LightGray, label = "color")
            
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

data class Testimonial(
    val name: String, 
    val location: String, 
    val text: String, 
    val imageRes: Int, 
    val yieldIcon: String
)
