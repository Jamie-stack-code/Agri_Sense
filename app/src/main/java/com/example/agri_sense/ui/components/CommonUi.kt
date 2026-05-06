package com.example.agri_sense.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.agri_sense.R
import com.example.agri_sense.ui.theme.PremiumDarkGreen
import com.example.agri_sense.ui.theme.PremiumGold

/**
 * Global Logo component for Agri-Sense.
 * 
 * To use your own logo image:
 * 1. Add your image (e.g. logo.png) to res/drawable
 * 2. Change 'useDefaultIcon' to false below and update the resource ID.
 */
@Composable
fun AgriSenseLogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    tint: Color = PremiumDarkGreen,
    useDefaultIcon: Boolean = false // CHANGE THIS TO FALSE TO USE YOUR IMAGE
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (useDefaultIcon) {
            Icon(
                imageVector = Icons.Default.Eco,
                contentDescription = "Agri-Sense Logo",
                tint = tint,
                modifier = Modifier.size(size)
            )
        } else {
             Image(
                 painter = painterResource(id = R.drawable.ag),
                 contentDescription = "Agri-Sense Logo",
                 modifier = Modifier.size(size)
             )
        }
    }
}
