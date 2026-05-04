package com.example.agri_sense.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.ui.theme.*

@Composable
fun LanguageScreen(onLanguageSelected: (String) -> Unit) {
    var selectedLanguage by remember { mutableStateOf("English") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumSurface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Premium Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PremiumDarkGreen, AgriGreenScreenBg)
                    ),
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
                .padding(top = 80.dp, bottom = 48.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.size(96.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = PremiumGold,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = if (selectedLanguage == "English") "Choose Language" else "Sankhani Chinenero",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (selectedLanguage == "English") 
                        "Select the language you are most comfortable with using for your farm management." 
                    else 
                        "Sankhani chinenero chomwe mungamasuke kugwiritsa ntchito poyang'anira munda wanu.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            PremiumLanguageOption(
                title = "English",
                subtitle = "Agricultural insights in English",
                isSelected = selectedLanguage == "English",
                onClick = { selectedLanguage = "English" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PremiumLanguageOption(
                title = "Chichewa",
                subtitle = "Uphungu wa ulimi mu Chichewa",
                isSelected = selectedLanguage == "Chichewa",
                onClick = { selectedLanguage = "Chichewa" }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Premium Glowing CTA
            Button(
                onClick = { onLanguageSelected(selectedLanguage) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = PremiumGold),
                colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                shape = RoundedCornerShape(32.dp)
            ) {
                Text(
                    text = if (selectedLanguage == "English") "Continue" else "Pitirizani",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (selectedLanguage == "English") "You can change this anytime in settings." else "Mukhoza kusintha izi nthawi ina iliyonse muzosankha.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = OnSurfaceSubtle
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PremiumLanguageOption(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.shadow(8.dp, RoundedCornerShape(24.dp), spotColor = PremiumDarkGreen)
                else Modifier
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) PremiumDarkGreen else Color.LightGray.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = if (isSelected) PremiumDarkGreen else Color(0xFFF0F0F0),
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (title == "English") "EN" else "CH",
                            color = if (isSelected) PremiumGold else Color.Gray,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isSelected) PremiumDarkGreen else Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = OnSurfaceSubtle,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = PremiumGold,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
