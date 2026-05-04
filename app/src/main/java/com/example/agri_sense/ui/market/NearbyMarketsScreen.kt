package com.example.agri_sense.ui.market

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Storefront
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.agri_sense.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyMarketsScreen(onBack: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = PremiumSurface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Nearby Markets", color = Color.White, fontWeight = FontWeight.Bold) },
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
            // Premium Hero Map Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PremiumDarkGreen, AgriGreenScreenBg)
                        ),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.padding(24.dp).fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = PremiumGold.copy(alpha = 0.2f),
                            shape = CircleShape,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(40.dp), tint = PremiumGold)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Live Interactive Map", 
                            color = Color.White, 
                            fontSize = 20.sp, 
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Searching for markets near you...", 
                            color = Color.White.copy(alpha = 0.8f), 
                            fontSize = 14.sp
                        )
                    }
                }
            }
            
            // Markets List Section
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Markets Near You", 
                    fontSize = 22.sp, 
                    fontWeight = FontWeight.Black,
                    color = PremiumDarkGreen
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                PremiumMarketListItem(
                    name = "Lilongwe Central Market", 
                    distance = "2.5 km away",
                    onNavigate = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Routing to Lilongwe Central Market...")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                PremiumMarketListItem(
                    name = "Area 25 Market", 
                    distance = "5.8 km away",
                    onNavigate = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Routing to Area 25 Market...")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                PremiumMarketListItem(
                    name = "Kanengo Market", 
                    distance = "8.2 km away",
                    onNavigate = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Routing to Kanengo Market...")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PremiumMarketListItem(name: String, distance: String, onNavigate: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onNavigate),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = PremiumDarkGreen.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Storefront, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = name, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = PremiumDarkGreen)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = distance, fontSize = 14.sp, color = OnSurfaceSubtle, fontWeight = FontWeight.Medium)
                    }
                }
            }
            
            // Premium Navigate Action
            Surface(
                color = PremiumGold.copy(alpha = 0.2f),
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Navigation, contentDescription = "Navigate", tint = PremiumDarkGreen, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
