package com.example.agri_sense.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun ProfileSetupScreen(onSetupFree: () -> Unit, onSetupPremium: () -> Unit) {
    val authViewModel: AuthViewModel = hiltViewModel()
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var locationExpanded by remember { mutableStateOf(false) }
    var farmSize by remember { mutableStateOf("") }
    var farmSizeExpanded by remember { mutableStateOf(false) }
    var selectedCrop by remember { mutableStateOf("") }
    var cropExpanded by remember { mutableStateOf(false) }
    
    var showFreeConfirmCard by remember { mutableStateOf(false) }
    var showPremiumConfirmCard by remember { mutableStateOf(false) }
    
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
                Icon(Icons.Default.Person, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(40.dp))
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
                PremiumFieldLabel(text = "Full Name (Optional)", icon = Icons.Default.Badge)
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
                PremiumFieldLabel(text = "District", icon = Icons.Default.LocationOn)
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
                PremiumFieldLabel(text = "Farm Size (Hectares)", icon = Icons.Default.SquareFoot)
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
                PremiumFieldLabel(text = "Primary Crop Type", icon = Icons.Default.Grass)
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
        
        // Free Trial Button
        OutlinedButton(
            onClick = { showFreeConfirmCard = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(32.dp),
            border = BorderStroke(2.dp, PremiumDarkGreen),
            enabled = location.isNotBlank() && selectedCrop.isNotBlank()
        ) {
            Text(
                text = "Finish Setup (Free 1-Month Trial)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = PremiumDarkGreen
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Premium Button
        Button(
            onClick = { showPremiumConfirmCard = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = PremiumGold),
            colors = ButtonDefaults.buttonColors(
                containerColor = PremiumDarkGreen,
                disabledContainerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(32.dp),
            enabled = location.isNotBlank() && selectedCrop.isNotBlank()
        ) {
            Text(
                text = "Finish Setup (Premium)",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = PremiumGold
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        }
    }
    
    // Interactive Confirmation Cards
    if (showFreeConfirmCard) {
        AlertDialog(
            onDismissRequest = { showFreeConfirmCard = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text("Start Free Trial", fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
            },
            text = {
                Column {
                    Text("You will get 30 days of full access to Agri-Sense to evaluate our AI tools.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("To continue, please sign in. You can upgrade to Premium anytime.", color = OnSurfaceSubtle, fontSize = 14.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showFreeConfirmCard = false
                        // Parse farm size from label to hectares
                        val farmSizeHa = parseFarmSize(farmSize)
                        authViewModel.saveProfile(
                            name = name.ifBlank { "Farmer" },
                            phone = "",
                            district = location,
                            farmSize = farmSizeHa,
                            crops = listOf(selectedCrop.split(" (").first()),
                            language = "English",
                            subscriptionStatus = "FREE_TRIAL"
                        )
                        onSetupFree()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen)
                ) {
                    Text("Sign In")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFreeConfirmCard = false }) {
                    Text("Cancel", color = OnSurfaceSubtle)
                }
            }
        )
    }

    if (showPremiumConfirmCard) {
        AlertDialog(
            onDismissRequest = { showPremiumConfirmCard = false },
            containerColor = PremiumDarkGreen,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text("Unlock Premium", fontWeight = FontWeight.ExtraBold, color = PremiumGold)
            },
            text = {
                Column {
                    Text("Get unlimited access to Agri-Sense for the entire farming season for only $3 (~5,200 MWK).", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("You will sign in, then proceed to payment via Airtel Money or TNM Mpamba.", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPremiumConfirmCard = false
                        val farmSizeHa = parseFarmSize(farmSize)
                        authViewModel.saveProfile(
                            name = name.ifBlank { "Farmer" },
                            phone = "",
                            district = location,
                            farmSize = farmSizeHa,
                            crops = listOf(selectedCrop.split(" (").first()),
                            language = "English",
                            subscriptionStatus = "PREMIUM"
                        )
                        onSetupPremium()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumGold)
                ) {
                    Text("Sign In & Pay", color = PremiumDarkGreen, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPremiumConfirmCard = false }) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.7f))
                }
            }
        )
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
