package com.example.agri_sense.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForecastScreen(onBack: () -> Unit) {
    val dailyForecast = listOf(
        Forecast("Mon", 24, "Sunny"),
        Forecast("Tue", 26, "Cloudy"),
        Forecast("Wed", 22, "Rain"),
        Forecast("Thu", 23, "Storm"),
        Forecast("Fri", 25, "Sunny"),
        Forecast("Sat", 27, "Hot"),
        Forecast("Sun", 24, "Sunny")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Intelligence", fontWeight = FontWeight.ExtraBold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PremiumDarkGreen)
            )
        },
        containerColor = PremiumSurface
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PremiumDarkGreen, PremiumSurface),
                        startY = 0f,
                        endY = 400f
                    )
                )
                .padding(24.dp)
        ) {
            // Hero Weather Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("LILONGWE", color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("24°", color = Color.White, fontSize = 80.sp, fontWeight = FontWeight.Black)
                    Text("Partly Cloudy", color = PremiumGold, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        WeatherDetailItem("Humidity", "65%")
                        WeatherDetailItem("Wind", "12km/h")
                        WeatherDetailItem("Rain", "10%")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text("7-Day Forecast", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(dailyForecast) { item ->
                    ForecastCard(item)
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Agriculture specific alerts
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                border = BorderStroke(1.dp, Color(0xFFFED7AA))
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color(0xFFEA580C))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Irrigation Alert", fontWeight = FontWeight.Bold, color = Color(0xFF9A3412))
                        Text("High evaporation expected Wed-Fri. Adjust watering schedule.", fontSize = 13.sp, color = Color(0xFFC2410C))
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastCard(item: Forecast) {
    Card(
        modifier = Modifier.width(80.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(item.day, fontWeight = FontWeight.Bold, color = OnSurfaceSubtle)
            Spacer(modifier = Modifier.height(8.dp))
            Text("🌦️", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("${item.temp}°", fontWeight = FontWeight.Black, fontSize = 18.sp, color = PremiumDarkGreen)
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
    }
}

data class Forecast(val day: String, val temp: Int, val condition: String)
