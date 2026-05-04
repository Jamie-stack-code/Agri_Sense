package com.example.agri_sense.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.example.agri_sense.ui.theme.*

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
    onNavigateToAskExpert: () -> Unit = {}
) {
    val isEnglish = language == "English"
    val viewModel: HomeViewModel = hiltViewModel()
    val farmer by viewModel.farmer.collectAsState()
    val weather by viewModel.weather.collectAsState()
    val unreadPestCount by viewModel.unreadPestCount.collectAsState()
    val aiResponse by viewModel.aiResponse.collectAsState()

    var showWeatherDetail by remember { mutableStateOf(false) }
    var showAskAgriSense by remember { mutableStateOf(false) }
    var chatInput by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val farmerName = farmer?.name ?: "Farmer"
    val farmerDistrict = farmer?.district ?: "Lilongwe"
    val weatherTemp = weather?.temperatureC?.toInt() ?: 24
    val weatherCondition = if (isEnglish) (weather?.condition ?: "Partly Cloudy") else (weather?.conditionChichewa?.ifEmpty { weather?.condition } ?: "Mitambo")

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAskAgriSense = true },
                containerColor = PremiumDarkGreen,
                contentColor = PremiumGold,
                modifier = Modifier.shadow(16.dp, RoundedCornerShape(16.dp), spotColor = PremiumGold),
                icon = { Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = PremiumGold) },
                text = { Text(if (isEnglish) "Ask Agri-Sense" else "Funsani Agri-Sense", fontWeight = FontWeight.Bold, color = Color.White) }
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
            // HERO HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PremiumDarkGreen, AgriGreenScreenBg)
                        ),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
                    .padding(top = 40.dp, bottom = 48.dp, start = 24.dp, end = 24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(56.dp).clickable { onNavigateToProfile() }
                            ) {
                                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White, modifier = Modifier.padding(12.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(if (isEnglish) "Good Morning," else "Mwauka Bwanji,", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                                Text(farmerName, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        // Language Toggle
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.clickable { onToggleLanguage() }
                        ) {
                            Text(
                                text = if (isEnglish) "EN" else "CH",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Premium Daily Weather Card
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { showWeatherDetail = true },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.WbCloudy, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(if (isEnglish) "$farmerDistrict • Today" else "$farmerDistrict • Lero", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Text(weatherCondition, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                                }
                            }
                            Text("${weatherTemp}°C", color = PremiumGold, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }

            // MAIN CONTENT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-24).dp)
                    .padding(horizontal = 24.dp)
            ) {
                // Quick Actions Grid
                Text(
                    text = if (isEnglish) "Farm Controls" else "Zoyang'anira Munda",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PremiumDarkGreen
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PremiumActionCard(
                            emoji = "🌱",
                            title = if (isEnglish) "Soil Analysis" else "Kuyeza Nthaka",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToSoil
                        )
                        PremiumActionCard(
                            emoji = "🌽",
                            title = if (isEnglish) "Crop Advice" else "Malangizo a Zomera",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToRecommendations
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PremiumActionCard(
                            emoji = "🌦",
                            title = if (isEnglish) "Weather Alerts" else "Zanyengo",
                            modifier = Modifier.weight(1f),
                            onClick = { showWeatherDetail = true }
                        )
                        PremiumActionCard(
                            emoji = "📈",
                            title = if (isEnglish) "Market Prices" else "Mitengo Pamsika",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToMarket
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PremiumActionCard(
                            emoji = "🐛",
                            title = if (isEnglish) "Pest Alerts" else "Machenjezo a Tizilombo",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToPestAlerts
                        )
                        PremiumActionCard(
                            emoji = "👨‍🔬",
                            title = if (isEnglish) "Ask Experts" else "Funsani Akatswiri",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToAskExpert
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Official African and Malawi Daily News Card
                Text(
                    text = if (isEnglish) "Agri-Sense Official Daily News" else "Nkhani ZauLimi Za Lero",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PremiumDarkGreen
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Public, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(if (isEnglish) "AIP Subsidized Fertilizer Updates" else "Nkhani za Fetereza ya AIP", fontWeight = FontWeight.Bold, color = PremiumDarkGreen, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isEnglish) 
                                "Ministry of Agriculture, Malawi: The Affordable Inputs Programme (AIP) distribution begins next week. Ensure your national ID is ready. \n\nRegional Africa Trend: Drought-resistant maize varieties are heavily recommended this season due to predicted El Niño effects." 
                            else 
                                "Unduna wa Zaulimi: Kugawidwa kwa fetereza ya AIP kuyamba sabata yamawa. Onetsetsani kuti muli ndi chiphaso chanu cha mzika.\n\nUphungu Wa ku Africa: Mbeu zachimanga zopirira chilala zikulimbikitsidwa chaka chino chifukwa cha El Niño.",
                            color = Color.DarkGray,
                            lineHeight = 22.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Expanded Weather Dialogue
        if (showWeatherDetail) {
            AlertDialog(
                onDismissRequest = { showWeatherDetail = false },
                title = { 
                    Text(
                        if (isEnglish) "Weather Forecast" else "Mvula Yamawa", 
                        fontWeight = FontWeight.ExtraBold,
                        color = PremiumDarkGreen
                    ) 
                },
                text = { 
                    Column {
                        Text(
                            if (isEnglish) "Weekly Outlook: Rain expected Mon-Wed. Dry spells late week." 
                            else "Mlungu Uno: Mvula Lolemba mpaka Lachitatu. Dzuwa kumapeto kwa sabata.",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            if (isEnglish) "Monthly Prediction: Above-average rainfall expected for the central region this month. Optimal for hybrid maize planting." 
                            else "Mwezi Uno: Mvula yambiri ikuyembekezeka m'chigawo chapakati. Nthawi yabwino yodzala chimanga."
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showWeatherDetail = false },
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Close", color = PremiumGold, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(24.dp)
            )
        }

        // Ask Agri-Sense Interactive Sheet
        if (showAskAgriSense) {
            ModalBottomSheet(
                onDismissRequest = { showAskAgriSense = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (isEnglish) "Ask Agri-Sense AI" else "Funsani Agri-Sense AI",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = PremiumDarkGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        if (isEnglish) "How can I help with your farm today?" else "Kodi ndingakuthandizeni bwanji pa munda wanu lero?",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    OutlinedTextField(
                        value = chatInput,
                        onValueChange = { chatInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(if (isEnglish) "Type your question..." else "Lembani funso lanu...") },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PremiumDarkGreen,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (chatInput.isNotBlank()) {
                                        viewModel.askAgriSense(chatInput, isEnglish)
                                        chatInput = ""
                                    }
                                }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = PremiumDarkGreen)
                            }
                        }
                    )

                    // AI Response Card
                    if (aiResponse.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PremiumDarkGreen.copy(alpha = 0.06f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.SmartToy, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Agri-Sense AI", fontWeight = FontWeight.Bold, color = PremiumDarkGreen, fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(aiResponse, color = Color.DarkGray, lineHeight = 20.sp, fontSize = 14.sp)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun PremiumActionCard(emoji: String, title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.aspectRatio(1f).clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PremiumDarkGreen.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = PremiumDarkGreen.copy(alpha = 0.12f),
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = emoji, fontSize = 32.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = PremiumDarkGreen,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
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
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontWeight = FontWeight.Bold, fontSize = 10.sp) },
            selected = currentRoute == "home",
            onClick = onNavigateToHome,
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PremiumDarkGreen, selectedTextColor = PremiumDarkGreen, indicatorColor = TagGreenBg)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Science, contentDescription = "Soil") },
            label = { Text("Soil", fontWeight = FontWeight.Bold, fontSize = 10.sp) },
            selected = currentRoute == "soil",
            onClick = onNavigateToSoil,
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PremiumDarkGreen, selectedTextColor = PremiumDarkGreen, indicatorColor = TagGreenBg)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Storefront, contentDescription = "Market") },
            label = { Text("Market", fontWeight = FontWeight.Bold, fontSize = 10.sp) },
            selected = currentRoute == "market",
            onClick = onNavigateToMarket,
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PremiumDarkGreen, selectedTextColor = PremiumDarkGreen, indicatorColor = TagGreenBg)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.People, contentDescription = "Community") },
            label = { Text("Network", fontWeight = FontWeight.Bold, fontSize = 10.sp) },
            selected = currentRoute == "community",
            onClick = onNavigateToCommunity,
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PremiumDarkGreen, selectedTextColor = PremiumDarkGreen, indicatorColor = TagGreenBg)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontWeight = FontWeight.Bold, fontSize = 10.sp) },
            selected = currentRoute == "profile",
            onClick = onNavigateToProfile,
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PremiumDarkGreen, selectedTextColor = PremiumDarkGreen, indicatorColor = TagGreenBg)
        )
    }
}
