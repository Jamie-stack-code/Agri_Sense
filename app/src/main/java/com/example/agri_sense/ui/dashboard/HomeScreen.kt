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
    onNavigateToRecommendations: () -> Unit,
    onNavigateToPestAlerts: () -> Unit = {},
    onNavigateToAskExpert: () -> Unit = {},
    onNavigateToWeather: () -> Unit = {}
) {
    val isEnglish = language == "English"
    val viewModel: HomeViewModel = hiltViewModel()
    val farmer by viewModel.farmer.collectAsState()
    val weather by viewModel.weather.collectAsState()
    val unreadPestCount by viewModel.unreadPestCount.collectAsState()
    val aiResponse by viewModel.aiResponse.collectAsState()

    var showAskAgriSense by remember { mutableStateOf(false) }
    var showWeatherDetail by remember { mutableStateOf(false) }
    var chatInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHost = remember { SnackbarHostState() }

    val name = farmer?.name ?: "Farmer"
    val district = farmer?.district ?: "Lilongwe"
    val temp = weather?.temperatureC?.toInt() ?: 24
    val humidity = weather?.humidity ?: 65
    val wind = weather?.windSpeedKmh?.toInt() ?: 12
    val condition = if (isEnglish) weather?.condition ?: "Partly Cloudy"
                   else weather?.conditionChichewa?.ifEmpty { weather?.condition } ?: "Mitambo"

    // Pulse animation for FAB glow
    val pulse = rememberInfiniteTransition(label = "fab")
    val glow by pulse.animateFloat(
        initialValue = 0.85f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "glow"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) },
        floatingActionButton = {
            Box(contentAlignment = Alignment.Center) {
                // Glow ring
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(glow)
                        .clip(CircleShape)
                        .background(PremiumGold.copy(alpha = 0.25f))
                )
                ExtendedFloatingActionButton(
                    onClick = { showAskAgriSense = true },
                    containerColor = PremiumDarkGreen,
                    contentColor = PremiumGold,
                    modifier = Modifier.shadow(12.dp, RoundedCornerShape(16.dp), spotColor = PremiumGold),
                    icon = { Icon(Icons.Default.AutoAwesome, null, tint = PremiumGold) },
                    text = {
                        Text(
                            if (isEnglish) "Ask Agri-Sense" else "Funsani Agri-Sense",
                            fontWeight = FontWeight.Bold, color = Color.White
                        )
                    }
                )
            }
        },
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
                            if (unreadPestCount > 0) {
                                BadgedBox(badge = { Badge(containerColor = PremiumAccentAmber) { Text("$unreadPestCount", fontSize = 10.sp, fontWeight = FontWeight.Bold) } }) {
                                    IconButton(onClick = onNavigateToPestAlerts) {
                                        Icon(Icons.Default.NotificationsActive, null, tint = PremiumGold)
                                    }
                                }
                            }
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
                    GradientActionCard("🌽", if (isEnglish) "Crop Advice" else "Malangizo", CardGradientCrop, Modifier.weight(1f), onNavigateToRecommendations)
                }
                Spacer(Modifier.height(14.dp))
                // Row 2
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    GradientActionCard("🌦️", if (isEnglish) "Weather" else "Nyengo", CardGradientWeather, Modifier.weight(1f)) { onNavigateToWeather() }
                    GradientActionCard("📈", if (isEnglish) "Market Prices" else "Mitengo", CardGradientMarket, Modifier.weight(1f), onNavigateToMarket)
                }
                Spacer(Modifier.height(14.dp))
                // Row 3
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    GradientActionCard("🐛", if (isEnglish) "Pest Alerts" else "Machenjezo", CardGradientPest, Modifier.weight(1f), onNavigateToPestAlerts)
                    GradientActionCard("👨‍🔬", if (isEnglish) "Ask Expert" else "Akatswiri", CardGradientExpert, Modifier.weight(1f), onNavigateToAskExpert)
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

                IntelCard(
                    tag = "🏛️ POLICY",
                    tagColor = PremiumDarkGreen,
                    title = if (isEnglish) "AIP Subsidized Fertilizer Distribution Begins" else "Kugawidwa kwa Fetereza ya AIP Kuyamba",
                    body = if (isEnglish) "Ministry of Agriculture: AIP distribution begins next week. Ensure your national ID is ready."
                    else "Unduna wa Zaulimi: Kugawidwa kwa fetereza kuyamba sabata yamawa. Onetsetsani kuti muli ndi chiphaso.",
                )
                Spacer(Modifier.height(12.dp))
                IntelCard(
                    tag = "🌍 AFRICA",
                    tagColor = PremiumTeal,
                    title = if (isEnglish) "Drought-Resistant Maize Recommended for 2025 Season" else "Chimanga Chopirira Chilala Chilimbikitsidwa",
                    body = if (isEnglish) "Regional trend: El Niño effects predicted. Switch to drought-tolerant varieties like SC403 or DK8031."
                    else "Mbeu zachimanga zopirira chilala zikulimbikitsidwa chifukwa cha El Niño.",
                )
                Spacer(Modifier.height(12.dp))
                IntelCard(
                    tag = "📊 MARKET",
                    tagColor = Color(0xFF6A1B9A),
                    title = if (isEnglish) "Groundnut Prices Up 13% in Southern Region" else "Mitengo ya Mtedza Yakwera 13%",
                    body = if (isEnglish) "Zomba and Blantyre markets recording best groundnut prices this week. Current: MK 1,520/kg."
                    else "Mitengo ya mtedza ku Zomba ndi Blantyre: MK 1,520/kg.",
                )

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
                    Button(onClick = { showWeatherDetail = false }, colors = ButtonDefaults.buttonColors(PremiumDarkGreen), shape = RoundedCornerShape(16.dp)) {
                        Text("OK", color = PremiumGold, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color.White, shape = RoundedCornerShape(24.dp)
            )
        }

        // ── ASK AGRI-SENSE SHEET ─────────────────────────────────────────────
        if (showAskAgriSense) {
            ModalBottomSheet(
                onDismissRequest = { showAskAgriSense = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(48.dp).clip(CircleShape).background(PremiumDarkGreen), Alignment.Center) {
                        Icon(Icons.Default.AutoAwesome, null, tint = PremiumGold, modifier = Modifier.size(24.dp))
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(if (isEnglish) "Ask Agri-Sense AI" else "Funsani Agri-Sense AI", fontSize = 22.sp, fontWeight = FontWeight.Black, color = PremiumDarkGreen)
                    Text(if (isEnglish) "Your smart farming assistant" else "Wothandiza wanu wolima", color = OnSurfaceSubtle, fontSize = 14.sp)
                    Spacer(Modifier.height(20.dp))
                    OutlinedTextField(
                        value = chatInput, onValueChange = { chatInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(if (isEnglish) "Ask about crops, weather, market…" else "Funsani za mbewu, nyengo, msika…") },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PremiumDarkGreen, unfocusedBorderColor = Color.LightGray),
                        trailingIcon = {
                            IconButton(onClick = {
                                if (chatInput.isNotBlank()) {
                                    scope.launch { viewModel.askAgriSense(chatInput, isEnglish); chatInput = "" }
                                }
                            }) { Icon(Icons.AutoMirrored.Filled.Send, null, tint = PremiumDarkGreen) }
                        }
                    )
                    if (aiResponse.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))
                        Card(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), CardDefaults.cardColors(PremiumDarkGreen.copy(.07f))) {
                            Column(Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.SmartToy, null, tint = PremiumDarkGreen, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Agri-Sense AI", fontWeight = FontWeight.Bold, color = PremiumDarkGreen, fontSize = 13.sp)
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(aiResponse, color = Color.DarkGray, lineHeight = 20.sp, fontSize = 14.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                }
            }
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
fun IntelCard(tag: String, tagColor: Color, title: String, body: String) {
    Card(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(20.dp),
        CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Surface(color = tagColor.copy(.1f), shape = RoundedCornerShape(8.dp)) {
                Text(tag, color = tagColor, fontSize = 11.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
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
