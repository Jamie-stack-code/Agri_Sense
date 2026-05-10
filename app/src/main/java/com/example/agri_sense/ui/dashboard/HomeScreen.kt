package com.example.agri_sense.ui.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.agri_sense.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    language: String,
    onToggleLanguage: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSoil: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToWeather: () -> Unit = {}
) {
    val isEnglish = language == "English"
    val viewModel: HomeViewModel = hiltViewModel()
    val farmer by viewModel.farmer.collectAsState()
    val weather by viewModel.weather.collectAsState()
    val recentDiscussions by viewModel.recentDiscussions.collectAsState()

    var showWeatherDetail by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHost = remember { SnackbarHostState() }

    val name = farmer?.name ?: "Farmer"
    val district = farmer?.district ?: "Lilongwe"
    val temp = weather?.temperatureC?.toInt() ?: 24
    val humidity = weather?.humidity ?: 65
    val wind = weather?.windSpeedKmh?.toInt() ?: 12
    val condition = if (isEnglish) weather?.condition ?: "Partly Cloudy"
                   else weather?.conditionChichewa?.ifEmpty { weather?.condition } ?: "Mitambo"

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) },
        bottomBar = {
            BottomNavBar(
                currentRoute = "home",
                onNavigateToHome = {},
                onNavigateToSoil = onNavigateToSoil,
                onNavigateToMarket = onNavigateToMarket,
                onNavigateToCommunity = onNavigateToCommunity,
                onNavigateToProfile = onNavigateToProfile
            )
        },
        containerColor = PremiumSurface
    ) { pad ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(pad)
                .verticalScroll(rememberScrollState())
        ) {

            // ── HERO HEADER ─────────────────────────────────────────────────
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(PremiumForest, PremiumDarkGreen, PremiumEmerald)),
                        RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp)
                    )
                    .padding(top = 40.dp, bottom = 48.dp, start = 24.dp, end = 24.dp)
            ) {
                Column {
                    // Top bar
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                Modifier.size(56.dp).clickable { onNavigateToProfile() },
                                CircleShape,
                                PremiumGold.copy(alpha = 0.18f)
                            ) {
                                Box(Modifier.fillMaxSize(), Alignment.Center) { Text("👨‍🌾", fontSize = 26.sp) }
                            }
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text(if (isEnglish) "Good Day," else "Mwauka Bwanji,", color = Color.White.copy(.7f), fontSize = 13.sp)
                                Text(name, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = Color.White.copy(.15f), shape = RoundedCornerShape(14.dp), modifier = Modifier.clickable { onToggleLanguage() }) {
                                Text(if (isEnglish) "EN" else "CH", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(28.dp))

                    // Weather card
                    Card(
                        Modifier.fillMaxWidth().clickable { onNavigateToWeather() },
                        RoundedCornerShape(24.dp),
                        CardDefaults.cardColors(Color.White.copy(.1f)),
                        border = BorderStroke(1.dp, Color.White.copy(.2f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(Modifier.padding(20.dp).fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Column {
                                Text("$temp°C", color = PremiumGold, fontSize = 52.sp, fontWeight = FontWeight.Black, lineHeight = 54.sp)
                                Text(condition, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                Text("$district • Today", color = Color.White.copy(.6f), fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                WeatherChip("💧", "$humidity%")
                                WeatherChip("🌬️", "$wind km/h")
                                WeatherChip("☀️", "UV ${weather?.uvIndex ?: 6}")
                            }
                        }
                    }
                }
            }

            // ── CONTENT ─────────────────────────────────────────────────────
            Column(Modifier.fillMaxWidth().offset(y = (-24).dp).padding(horizontal = 20.dp)) {

                Text(
                    if (isEnglish) "Farm Intelligence" else "Chidziwitso cha Munda",
                    fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen
                )
                Spacer(Modifier.height(14.dp))

                // Row 1
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    GradientActionCard("🌱", if (isEnglish) "Soil Analysis" else "Kuyeza Nthaka", CardGradientSoil, Modifier.weight(1f), onNavigateToSoil)
                    GradientActionCard("🌦️", if (isEnglish) "Weather" else "Nyengo", CardGradientWeather, Modifier.weight(1f)) { onNavigateToWeather() }
                }
                Spacer(Modifier.height(14.dp))
                // Row 2
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    GradientActionCard("📈", if (isEnglish) "Market Prices" else "Mitengo", CardGradientMarket, Modifier.weight(1f), onNavigateToMarket)
                    GradientActionCard("👨‍🔬", if (isEnglish) "Ask Expert" else "Akatswiri", CardGradientExpert, Modifier.weight(1f), onNavigateToCommunity)
                }

                if (recentDiscussions.isNotEmpty()) {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        if (isEnglish) "Recent Expert Solutions" else "Mayankho a Akatswiri",
                        fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen
                    )
                    Spacer(Modifier.height(14.dp))
                    recentDiscussions.forEach { disc ->
                        IntelCard(
                            tag = if (disc.authorCrop.isNotEmpty()) "✅ ${disc.authorCrop.uppercase()}" else "✅ EXPERT",
                            tagColor = PremiumTeal,
                            title = disc.question,
                            body = disc.expertAnswer,
                            timestamp = "Expert Verified"
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    TextButton(onClick = onNavigateToCommunity, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(if (isEnglish) "View All Discussions →" else "Onani Zonse →", color = PremiumDarkGreen, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(32.dp))

                // ── AGRI INTEL FEED ─────────────────────────────────────────
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Text(if (isEnglish) "Agri Intel Feed" else "Nkhani ZauLimi", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
                    Surface(color = PremiumAccentAmber.copy(.15f), shape = RoundedCornerShape(12.dp)) {
                        Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(6.dp).clip(CircleShape).background(Color.Red))
                            Spacer(Modifier.width(6.dp))
                            Text("LIVE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = PremiumDarkGreen)
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))

                val liveNews by viewModel.liveNews.collectAsState()

                if (liveNews.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PremiumDarkGreen)
                    }
                } else {
                    liveNews.forEach { news ->
                        val color = when (news.tagColor) {
                            "PremiumDarkGreen" -> PremiumDarkGreen
                            "PremiumTeal" -> PremiumTeal
                            else -> try { Color(android.graphics.Color.parseColor(news.tagColor)) } catch (e: Exception) { PremiumDarkGreen }
                        }
                        IntelCard(
                            tag = news.tag,
                            tagColor = color,
                            title = if (isEnglish) news.title else news.titleChichewa,
                            body = if (isEnglish) news.body else news.bodyChichewa,
                            timestamp = news.timestamp ?: "Today"
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }

                Spacer(Modifier.height(88.dp))
            }
        }

        // ── WEATHER DETAIL DIALOG ────────────────────────────────────────────
        if (showWeatherDetail) {
            AlertDialog(
                onDismissRequest = { showWeatherDetail = false },
                title = { Text(if (isEnglish) "Weather Forecast" else "Mvula Yamawa", fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (isEnglish) "Weekly Outlook: Rain expected Mon–Wed. Dry spells late week." else "Mvula Lolemba mpaka Lachitatu. Dzuwa kumapeto.", fontWeight = FontWeight.Bold)
                        Text(if (isEnglish) "Monthly: Above-average rainfall for Central region. Optimal for hybrid maize planting." else "Mvula yambiri m'chigawo chapakati. Nthawi yabwino yodzala chimanga.")
                    }
                },
                confirmButton = {
                    Button(onClick = { showWeatherDetail = false }, colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen, contentColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                        Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color.White, shape = RoundedCornerShape(24.dp)
            )
        }

    }
}

@Composable
fun WeatherChip(emoji: String, label: String) {
    Surface(color = Color.White.copy(.12f), shape = RoundedCornerShape(10.dp)) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 12.sp)
            Spacer(Modifier.width(4.dp))
            Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun GradientActionCard(emoji: String, title: String, gradient: List<Color>, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }
    val scope = rememberCoroutineScope()
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale.value)
            .clickable {
                scope.launch {
                    scale.animateTo(.93f, tween(80))
                    scale.animateTo(1f, tween(120))
                    onClick()
                }
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradient))
                .padding(16.dp)
        ) {
            Column(Modifier.fillMaxSize()) {
                Surface(Modifier.size(44.dp), CircleShape, Color.White.copy(.18f)) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) { Text(emoji, fontSize = 22.sp) }
                }
                Spacer(Modifier.weight(1f))
                Text(title, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, lineHeight = 16.sp, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun IntelCard(tag: String, tagColor: Color, title: String, body: String, timestamp: String = "Today") {
    Card(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(20.dp),
        CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Surface(color = tagColor.copy(.1f), shape = RoundedCornerShape(8.dp)) {
                    Text(tag, color = tagColor, fontSize = 11.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
                Text(timestamp, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.height(10.dp))
            Text(title, fontWeight = FontWeight.Bold, color = PremiumDarkGreen, fontSize = 15.sp, lineHeight = 20.sp)
            Spacer(Modifier.height(6.dp))
            Text(body, color = Color.DarkGray, fontSize = 13.sp, lineHeight = 19.sp)
        }
    }
}

@Composable
fun PremiumActionCard(emoji: String, title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    GradientActionCard(emoji, title, CardGradientSoil, modifier, onClick)
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigateToHome: () -> Unit,
    onNavigateToSoil: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    NavigationBar(containerColor = Color.White, tonalElevation = 12.dp) {
        val items = listOf(
            Triple("home", Icons.Default.Home, "Home"),
            Triple("soil", Icons.Default.Science, "Soil"),
            Triple("market", Icons.Default.Storefront, "Market"),
            Triple("community", Icons.Default.People, "Network"),
            Triple("profile", Icons.Default.Person, "Profile")
        )
        val actions = listOf(onNavigateToHome, onNavigateToSoil, onNavigateToMarket, onNavigateToCommunity, onNavigateToProfile)
        items.forEachIndexed { i, (route, icon, label) ->
            NavigationBarItem(
                icon = { Icon(icon, label) },
                label = { Text(label, fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                selected = currentRoute == route,
                onClick = actions[i],
                colors = NavigationBarItemDefaults.colors(selectedIconColor = PremiumDarkGreen, selectedTextColor = PremiumDarkGreen, indicatorColor = TagGreenBg)
            )
        }
    }
}
