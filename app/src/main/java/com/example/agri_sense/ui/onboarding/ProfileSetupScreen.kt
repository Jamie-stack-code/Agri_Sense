package com.example.agri_sense.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun ProfileSetupScreen(onSetupComplete: () -> Unit) {
    val authViewModel: AuthViewModel = hiltViewModel()
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var locationExpanded by remember { mutableStateOf(false) }
    var farmSize by remember { mutableStateOf("") }
    var farmSizeExpanded by remember { mutableStateOf(false) }
    var selectedCrop by remember { mutableStateOf("") }
    var cropExpanded by remember { mutableStateOf(false) }
    
    val districts = listOf(
        "Balaka", "Blantyre", "Chikwawa", "Chiradzulu", "Chitipa", "Dedza", "Dowa",
        "Karonga", "Kasungu", "Lilongwe", "Machinga", "Mangochi", "Mchinji", "Mulanje",
        "Mwanza", "Mzimba", "Neno", "Nkhata Bay", "Nkhotakota", "Nsanje",
        "Ntcheu", "Ntchisi", "Phalombe", "Rumphi", "Salima", "Thyolo", "Zomba"
    )
    
    val farmSizes = listOf(
        "Small (Less than 1 Ha)",
        "Medium (1 - 5 Ha)",
        "Large (5 - 10 Ha)",
        "Commercial (Above 10 Ha)"
    )
    
    val crops = listOf(
        "Maize (Chimanga)", 
        "Tobacco (Fodya)", 
        "Groundnuts (Mtedza)", 
        "Soybeans (Soya)", 
        "Rice (Mpunga)", 
        "Beans (Nyemba)"
    )

    Scaffold(
        containerColor = PremiumSurface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Header
        Surface(
            color = PremiumDarkGreen.copy(alpha = 0.1f),
            shape = CircleShape,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(40.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Create Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = PremiumDarkGreen
        )
        Text(
            text = "Tell us about your farm",
            color = OnSurfaceSubtle,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Field 1: Full Name
                PremiumFieldLabel(text = "Full Name (Optional)", icon = Icons.Filled.Badge)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("e.g. John Banda", color = OnSurfaceSubtle) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = premiumTextFieldColors(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Field 2: Location
                PremiumFieldLabel(text = "District", icon = Icons.Filled.LocationOn)
                ExposedDropdownMenuBox(
                    expanded = locationExpanded,
                    onExpandedChange = { locationExpanded = !locationExpanded }
                ) {
                    OutlinedTextField(
                        value = location,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select District", color = OnSurfaceSubtle) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                        shape = RoundedCornerShape(16.dp),
                        colors = premiumTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = locationExpanded,
                        onDismissRequest = { locationExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        districts.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption, color = Color.Black) },
                                onClick = {
                                    location = selectionOption
                                    locationExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Field 3: Farm Size
                PremiumFieldLabel(text = "Farm Size (Hectares)", icon = Icons.Filled.SquareFoot)
                ExposedDropdownMenuBox(
                    expanded = farmSizeExpanded,
                    onExpandedChange = { farmSizeExpanded = !farmSizeExpanded }
                ) {
                    OutlinedTextField(
                        value = farmSize,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select Farm Size", color = OnSurfaceSubtle) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = farmSizeExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                        shape = RoundedCornerShape(16.dp),
                        colors = premiumTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = farmSizeExpanded,
                        onDismissRequest = { farmSizeExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        farmSizes.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption, color = Color.Black) },
                                onClick = {
                                    farmSize = selectionOption
                                    farmSizeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Field 4: Crop Type
                PremiumFieldLabel(text = "Primary Crop Type", icon = Icons.Filled.Grass)
                ExposedDropdownMenuBox(
                    expanded = cropExpanded,
                    onExpandedChange = { cropExpanded = !cropExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCrop,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select Crop", color = OnSurfaceSubtle) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                        shape = RoundedCornerShape(16.dp),
                        colors = premiumTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = cropExpanded,
                        onDismissRequest = { cropExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        crops.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption, color = Color.Black) },
                                onClick = {
                                    selectedCrop = selectionOption
                                    cropExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
        
        // Save Profile Button
        Button(
            onClick = {
                val farmSizeHa = parseFarmSize(farmSize)
                authViewModel.saveProfile(
                    name = name.ifBlank { "Farmer" },
                    phone = "",
                    district = location,
                    farmSize = farmSizeHa,
                    crops = listOf(selectedCrop.split(" (").first()),
                    language = "English",
                    subscriptionStatus = "FREE" // Or whatever plan they actually selected
                )
                onSetupComplete()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = PremiumGold),
            colors = ButtonDefaults.buttonColors(
                containerColor = PremiumDarkGreen,
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(32.dp),
            enabled = location.isNotBlank() && selectedCrop.isNotBlank()
        ) {
            Text(
                text = "Complete Setup & Go to Dashboard",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PremiumFieldLabel(text: String, icon: ImageVector) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontWeight = FontWeight.Bold, color = PremiumDarkGreen, fontSize = 14.sp)
    }
}

@Composable
fun premiumTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedBorderColor = PremiumDarkGreen,
    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White
)

private fun parseFarmSize(label: String): Double = when {
    label.contains("Less than 1") -> 0.5
    label.contains("1 - 5") -> 3.0
    label.contains("5 - 10") -> 7.5
    label.contains("Above 10") -> 15.0
    else -> 1.0
}
