package com.example.agri_sense.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvisoryScreen(onBack: () -> Unit) {
    val advisories = listOf(
        AdvisoryItem("Fertilizer Application", "Top dressing application for maize should start now in the central region.", Icons.Default.Science, "FERTILIZER"),
        AdvisoryItem("Pest Alert", "High risk of Fall Armyworm detected in Lilongwe districts.", Icons.Default.BugReport, "PEST"),
        AdvisoryItem("Seasonal Planting", "Optimal time for planting drought-resistant legumes begins this week.", Icons.Default.Grass, "CROP"),
        AdvisoryItem("Weather Update", "Unusual dry spell expected next week. Plan for irrigation if possible.", Icons.Default.WbSunny, "WEATHER")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expert Advisories", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PremiumSurface)
            )
        },
        containerColor = PremiumSurface
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 48.dp)
        ) {
            items(advisories) { item ->
                AdvisoryCard(item)
            }
        }
    }
}

@Composable
fun AdvisoryCard(item: AdvisoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = PremiumDarkGreen.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(item.icon, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(28.dp))
                }
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Column {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PremiumDarkGreen
                )
                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = OnSurfaceSubtle,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

data class AdvisoryItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val category: String
)
