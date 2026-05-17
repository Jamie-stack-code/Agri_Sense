package com.example.agri_sense.ui.soilanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.data.models.SoilAnalysis
import com.example.agri_sense.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoilHistoryScreen(
    viewModel: SoilViewModel,
    language: String,
    onBack: () -> Unit
) {
    val isEnglish = language == "English"
    val history by viewModel.allAnalyses.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEnglish) "Soil Test History" else "Mbiri ya Kuyeza Nthaka", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = PremiumSurface
    ) { padding ->
        if (history.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Science, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(Modifier.height(16.dp))
                    Text(if (isEnglish) "No tests found." else "Palibe zotsatira.", color = OnSurfaceSubtle)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history) { analysis ->
                    HistoryItem(analysis, isEnglish)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(analysis: SoilAnalysis, isEnglish: Boolean) {
    val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(analysis.timestamp))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(analysis.soilType, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen, fontSize = 18.sp)
                Surface(
                    color = if (analysis.status == "EXPERT_COMPLETED") TagGreenBg else PremiumGold.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (analysis.status == "EXPERT_COMPLETED") (if (isEnglish) "REVIEWED" else "AYANKHIDWA")
                        else (if (isEnglish) "PENDING" else "ZIKUYEZEDWA"),
                        color = if (analysis.status == "EXPERT_COMPLETED") PremiumDarkGreen else PremiumGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Text(date, fontSize = 12.sp, color = OnSurfaceSubtle)
            Spacer(Modifier.height(12.dp))
            Text(
                analysis.generalRecommendation,
                maxLines = 2,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
            
            if (analysis.expertComment != null) {
                Spacer(Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = PremiumDarkGreen.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("👨‍🔬", fontSize = 16.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(analysis.expertComment, fontSize = 13.sp, color = PremiumDarkGreen, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
