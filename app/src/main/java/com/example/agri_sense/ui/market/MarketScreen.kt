package com.example.agri_sense.ui.market

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import android.content.pm.PackageManager
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import com.example.agri_sense.ui.dashboard.BottomNavBar
import com.example.agri_sense.ui.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    language: String,
    onBack: () -> Unit,
    onNavigateToNearby: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSoil: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showRouteOverlay by remember { mutableStateOf(false) }
    var targetMarket by remember { mutableStateOf("") }
    var showAlertsDialog by remember { mutableStateOf(false) }
    var alertCrop by remember { mutableStateOf("") }
    var alertPrice by remember { mutableStateOf("") }
    val isEnglish = language == "English"
    val viewModel: MarketViewModel = hiltViewModel()
    val profileViewModel: com.example.agri_sense.ui.profile.ProfileViewModel = hiltViewModel()
    
    val farmer by profileViewModel.farmer.collectAsState()
    val filteredPrices by viewModel.filteredPrices.collectAsState()
    val topGainers = remember(filteredPrices) { filteredPrices.sortedByDescending { it.trendPercent }.take(3) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavBar(
                currentRoute = "market",
                onNavigateToHome = onNavigateToHome,
                onNavigateToSoil = onNavigateToSoil,
                onNavigateToMarket = {},
                onNavigateToCommunity = onNavigateToCommunity,
                onNavigateToProfile = onNavigateToProfile
            )
        },
        containerColor = SurfaceLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Premium Gradient Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PremiumDarkGreen, AgriGreenScreenBg)
                        ),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
                    .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = if (isEnglish) "Market Prices" else "Mitengo ya Pamsika",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (isEnglish) "Live insights from 50+ African markets" else "Mitengo yeniyeni kuchokera m'misika 50+",
                    color = PremiumGold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp),
                    fontWeight = FontWeight.Medium
                )

                // Premium Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isEnglish) "Search crops or markets..." else "Fufuzani zomera...", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PremiumDarkGreen) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            // Update ViewModel search when search changes
            LaunchedEffect(searchQuery) { viewModel.setSearchQuery(searchQuery) }

            // Daily Best Prices Carousel
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-20).dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    items(topGainers) { price ->
                        val cropEmoji = when {
                            price.cropName.contains("Tobacco", true) -> "🌿"
                            price.cropName.contains("Maize", true) -> "🌽"
                            price.cropName.contains("Groundnut", true) -> "🥜"
                            price.cropName.contains("Soybean", true) -> "🫘"
                            price.cropName.contains("Rice", true) -> "🌾"
                            price.cropName.contains("Bean", true) -> "🫘"
                            price.cropName.contains("Cotton", true) -> "☁️"
                            else -> "🌱"
                        }
                        PremiumPriceCard(
                            emoji = cropEmoji,
                            crop = if (isEnglish) price.cropName else price.cropNameChichewa,
                            type = price.marketName,
                            price = "K${String.format(Locale.US, "%,.0f", price.pricePerKg)}",
                            trend = "+${String.format(Locale.US, "%.0f", price.trendPercent)}%",
                            market = "${price.district} • ${price.region}",
                            onRouteClick = {
                                targetMarket = price.marketName
                                showRouteOverlay = true
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isEnglish) "Explore all district markets →" else "Onani misika yonse →",
                    color = PremiumDarkGreen,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().clickable { onNavigateToNearby() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Official Market News Button
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://fpma.fao.org/giews/fpmat4/#/dashboard/tool/domestic"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PremiumGold, 
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isEnglish) "Live FAO Market News" else "Nkhani za Msika (FAO)", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Nearby Markets Section
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(if (isEnglish) "Trending Nearby" else "Misika Yapafupi", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(horizontal = 24.dp)) {
                    item {
                        PremiumNearbyMarketCard(if (isEnglish) "Lilongwe Main" else "Lilongwe Mkulu", if (isEnglish) "Central" else "Chigawo Chapakati", "Live", PremiumDarkGreen)
                    }
                    item {
                        PremiumNearbyMarketCard("Blantyre Hub", if (isEnglish) "Southern" else "Kumwera", "300km away", Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Premium Alert Card
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PremiumDarkGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = CircleShape,
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(28.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(if (isEnglish) "Smart Price Alerts" else "Chenjezo la Mtengo", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(if (isEnglish) "Let AI track the market for you" else "Dziwani mitengo ikasintha", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                        Button(
                            onClick = { showAlertsDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PremiumGold, 
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isEnglish) "Set Alert" else "Tcheru", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
                }
                
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Live Route Overlay Simulation
        if (showRouteOverlay) {
            AlertDialog(
                onDismissRequest = { showRouteOverlay = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Navigation, contentDescription = null, tint = PremiumDarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isEnglish) "Live Route" else "Njira Yapamapu", fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
                    }
                },
                text = {
                    Column {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp)).background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📍 Map View to $targetMarket", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("ETA: 45 minutes (Traffic Light)", fontWeight = FontWeight.Bold)
                        Text("Routing you to $targetMarket via M1 Road for the best trade-in prices today.", color = Color.Gray, fontSize = 14.sp)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { 
                            showRouteOverlay = false 
                            val gmmIntentUri = Uri.parse("google.navigation:q=$targetMarket+Market+Malawi")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                // Fallback if maps app not installed
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$targetMarket+Market+Malawi")))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PremiumGold, 
                            contentColor = Color.White
                        ),
                    ) {
                        Text(if (isEnglish) "Start Navigation" else "Yambani Ulendo", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRouteOverlay = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }

        // Price Alerts Dialog
        if (showAlertsDialog) {
            AlertDialog(
                onDismissRequest = { showAlertsDialog = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                title = {
                    Text(if (isEnglish) "Set Price Alert" else "Khazikitsani Chenjezo", fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("We will notify you immediately when prices reach your target.", color = Color.Gray, fontSize = 14.sp)
                        OutlinedTextField(
                            value = alertCrop,
                            onValueChange = { alertCrop = it },
                            label = { Text("Crop (e.g. Maize)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = PremiumDarkGreen
                            )
                        )
                        OutlinedTextField(
                            value = alertPrice,
                            onValueChange = { alertPrice = it },
                            label = { Text("Target Price (MWK)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = PremiumDarkGreen
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val id = farmer?.id ?: 0
                            if (alertCrop.isNotBlank() && alertPrice.isNotBlank()) {
                                viewModel.setPriceAlert(id.toString(), alertCrop, alertPrice.toDoubleOrNull() ?: 0.0)
                                showAlertsDialog = false
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (isEnglish) "Alert set for $alertCrop!" else "Chenjezo latsimikizika pa $alertCrop!"
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen)
                    ) {
                        Text("Save Alert", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAlertsDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
fun PremiumPriceCard(emoji: String, crop: String, type: String, price: String, trend: String, market: String, onRouteClick: () -> Unit) {
    Card(
        modifier = Modifier.width(320.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = PremiumDarkGreen.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(emoji, fontSize = 28.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(crop, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = PremiumDarkGreen)
                        Text(type, color = OnSurfaceMedium, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
                
                // Animated Trending Indicator
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("Best Price", color = OnSurfaceMedium, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(price, fontWeight = FontWeight.Black, fontSize = 24.sp, color = PremiumDarkGreen)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(16.dp).scale(scale))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(trend, color = PremiumGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), color = PremiumDarkGreen.copy(alpha = 0.12f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(market, color = OnSurfaceStrong, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onRouteClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PremiumDarkGreen,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Directions, contentDescription = null, tint = PremiumGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Explore & Route", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun PremiumNearbyMarketCard(name: String, region: String, status: String, statusColor: Color) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = SurfaceLight,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Storefront, contentDescription = null, tint = PremiumDarkGreen)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(name, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = PremiumDarkGreen, textAlign = TextAlign.Center)
            Text(region, color = OnSurfaceMedium, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = statusColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = status, 
                    color = statusColor, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
