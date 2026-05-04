package com.example.agri_sense.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.agri_sense.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val language by viewModel.language.collectAsState()
    val isEnglish = language == "English"
    val notifyPests by viewModel.notifyPest.collectAsState()
    val notifyMarket by viewModel.notifyMarket.collectAsState()
    val notifyWeather by viewModel.notifyWeather.collectAsState()
    val offlineMode by viewModel.offlineMode.collectAsState()

    Scaffold(
        containerColor = PremiumSurface,
        topBar = {
            TopAppBar(
                title = { Text("App Settings", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PremiumDarkGreen)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Localization Module
            SettingsSectionTitle("Localization")
            SettingsCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SettingsIcon(Icons.Default.Language, PremiumGold)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Language", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PremiumDarkGreen)
                            Text(if (isEnglish) "English" else "Chichewa", fontSize = 13.sp, color = OnSurfaceSubtle)
                        }
                    }
                    Switch(
                        checked = isEnglish,
                        onCheckedChange = {
                            viewModel.setLanguage(if (it) "English" else "Chichewa")
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PremiumDarkGreen,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }
            }

            // Smart Notifications Module
            SettingsSectionTitle("Smart Notifications")
            SettingsCard {
                Column {
                    SettingsToggleRow(
                        title = "Pest Outbreak Alerts",
                        subtitle = "Immediate warnings for your region",
                        icon = Icons.Default.BugReport,
                        iconTint = Color(0xFFD32F2F),
                        checked = notifyPests,
                        onCheckedChange = { viewModel.setNotifyPest(it) }
                    )
                    HorizontalDivider(color = SurfaceLight)
                    SettingsToggleRow(
                        title = "Market Price Spikes",
                        subtitle = "Alerts when your crops hit high prices",
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        iconTint = PremiumGold,
                        checked = notifyMarket,
                        onCheckedChange = { viewModel.setNotifyMarket(it) }
                    )
                    HorizontalDivider(color = SurfaceLight)
                    SettingsToggleRow(
                        title = "Severe Weather Warnings",
                        subtitle = "Floods, droughts, or extreme heat",
                        icon = Icons.Default.Thunderstorm,
                        iconTint = Color(0xFF1976D2),
                        checked = notifyWeather,
                        onCheckedChange = { viewModel.setNotifyWeather(it) }
                    )
                }
            }

            // Data & Connectivity Module
            SettingsSectionTitle("Data & Connectivity")
            SettingsCard {
                SettingsToggleRow(
                    title = "Offline Mode",
                    subtitle = "Auto-download Market & Weather data for offline use",
                    icon = Icons.Default.WifiOff,
                    iconTint = PremiumDarkGreen,
                    checked = offlineMode,
                    onCheckedChange = { viewModel.setOfflineMode(it) }
                )
            }

            // Support & Security Module
            SettingsSectionTitle("Support & Security")
            SettingsCard {
                Column {
                    SettingsClickableRow(
                        title = "Contact Agronomist Support",
                        icon = Icons.Default.SupportAgent,
                        iconTint = PremiumDarkGreen,
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider(color = SurfaceLight)
                    SettingsClickableRow(
                        title = "Privacy Policy",
                        icon = Icons.Default.Security,
                        iconTint = Color.Gray,
                        onClick = { /* TODO */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = OnSurfaceSubtle,
        letterSpacing = 1.sp
    )
}

@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.LightGray),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}

@Composable
fun SettingsIcon(icon: ImageVector, tint: Color) {
    Surface(
        color = tint.copy(alpha = 0.1f),
        shape = CircleShape,
        modifier = Modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsIcon(icon, iconTint)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PremiumDarkGreen)
                Text(subtitle, fontSize = 12.sp, color = OnSurfaceSubtle, lineHeight = 16.sp)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PremiumDarkGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun SettingsClickableRow(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SettingsIcon(icon, iconTint)
                Spacer(modifier = Modifier.width(16.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PremiumDarkGreen)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}
