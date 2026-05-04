package com.example.agri_sense.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.example.agri_sense.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerProfileScreen(
    onBack: () -> Unit,
    onNavigateToPayment: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val farmer by viewModel.farmer.collectAsState()
    val isPremium by viewModel.isPremium.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val farmerName = farmer?.name ?: "Farmer"
    val farmerDistrict = farmer?.district ?: "Lilongwe"
    val farmerRegion = farmer?.region ?: "Central"
    val farmSize = farmer?.farmSize ?: 0.0
    val primaryCrop = farmer?.cropsGrown?.split(",")?.firstOrNull()?.trim() ?: "—"
    val activeDays = viewModel.getActiveDays()

    Scaffold(
        containerColor = PremiumSurface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("My Profile", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PremiumDarkGreen),
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = PremiumGold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Premium Hero Profile Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PremiumDarkGreen, AgriGreenScreenBg)
                        ),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
                    .padding(bottom = 40.dp, top = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Surface(
                            modifier = Modifier.size(128.dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("👨‍🌾", fontSize = 64.sp)
                            }
                        }
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = PremiumGold,
                            shadowElevation = 4.dp,
                            onClick = onNavigateToEditProfile
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(farmerName, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("$farmerDistrict, $farmerRegion Region", color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                    if (isPremium) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = PremiumGold,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "⭐ PREMIUM",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                color = PremiumDarkGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PremiumStatCard("Farm Size", "${String.format("%.1f", farmSize)} Ha", Icons.Default.Terrain, modifier = Modifier.weight(1f))
                PremiumStatCard("Active", "${activeDays}d", Icons.Default.Timeline, modifier = Modifier.weight(1f))
            }

            // Information List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Account Overview", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
                Spacer(modifier = Modifier.height(4.dp))
                
                PremiumProfileInfoRow(
                    label = "Primary Crop",
                    value = primaryCrop,
                    icon = Icons.Default.Grass
                )
                PremiumProfileInfoRow(
                    label = "Soil Health",
                    value = "Good (Last checked 2d ago)",
                    icon = Icons.Default.HealthAndSafety
                )
                PremiumProfileInfoRow(
                    label = "Market Membership",
                    value = "$farmerDistrict Hub",
                    icon = Icons.Default.Storefront
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Subscription Module
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Subscription Status", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PremiumDarkGreen),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("FREE TRIAL ACTIVE", color = PremiumGold, fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("You have 29 days left on your trial.", color = Color.White, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Upgrade to a Full Season Pass to guarantee uninterrupted access.", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                    }
                }
            }



            Spacer(modifier = Modifier.height(40.dp))

            // Action Buttons
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Premium Upgrade Button
                Button(
                    onClick = onNavigateToPayment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = PremiumGold),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = PremiumGold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Upgrade to Premium", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White, letterSpacing = 1.sp)
                    }
                }
                
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = RoundedCornerShape(32.dp),
                    border = BorderStroke(2.dp, Color.Red.copy(alpha = 0.5f))
                ) {
                    Text("Log Out", color = Color.Red.copy(alpha = 0.8f), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun PremiumStatCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.shadow(8.dp, RoundedCornerShape(24.dp), spotColor = PremiumDarkGreen),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = PremiumDarkGreen.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(value, fontWeight = FontWeight.Black, fontSize = 22.sp, color = PremiumDarkGreen)
            Text(label, fontSize = 13.sp, color = OnSurfaceSubtle, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun PremiumProfileInfoRow(label: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = PremiumGold.copy(alpha = 0.2f),
                shape = CircleShape,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(label, fontSize = 13.sp, color = OnSurfaceSubtle, fontWeight = FontWeight.Medium)
                Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
            }
        }
    }
}
