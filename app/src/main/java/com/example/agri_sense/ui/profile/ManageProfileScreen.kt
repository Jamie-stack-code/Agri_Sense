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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.agri_sense.ui.theme.*

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ManageProfileScreen(
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val farmer by viewModel.farmer.collectAsState()
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var farmSize by remember { mutableStateOf(2.5f) }
    var location by remember { mutableStateOf("") }
    var showAvatarDialog by remember { mutableStateOf(false) }

    val allCrops = listOf("Maize", "Tobacco", "Groundnuts", "Soybeans", "Cassava", "Beans", "Cotton")
    val selectedCrops = remember { mutableStateListOf<String>() }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            try {
                context.contentResolver.takePersistableUriPermission(uri, flag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            viewModel.updateAvatarUri(uri.toString())
        }
    }

    // Load farmer data once when available
    LaunchedEffect(farmer) {
        farmer?.let { f ->
            fullName = f.name
            phone = f.phone
            farmSize = f.farmSize.toFloat()
            location = "${f.district}, ${f.region} Region"
            selectedCrops.clear()
            selectedCrops.addAll(f.cropsGrown.split(",").map { it.trim() }.filter { it.isNotEmpty() })
        }
    }

    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text("Profile Picture") },
            text = { Text("Choose what you want to do with your profile picture.") },
            confirmButton = {
                TextButton(onClick = {
                    showAvatarDialog = false
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) { Text("Upload New") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAvatarDialog = false
                    viewModel.updateAvatarUri("")
                }) { Text("Remove") }
            }
        )
    }

    Scaffold(
        containerColor = PremiumSurface,
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", color = Color.White, fontWeight = FontWeight.Bold) },
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
            // Hero Image Editor
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
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        modifier = Modifier.size(120.dp).clickable { showAvatarDialog = true },
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        if (farmer?.avatarUri.isNullOrEmpty()) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("👨‍🌾", fontSize = 60.sp)
                            }
                        } else {
                            AsyncImage(
                                model = farmer?.avatarUri,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = PremiumGold,
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Personal Details Module
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text("Personal Details", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PremiumDarkGreen) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = PremiumDarkGreen,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = PremiumDarkGreen) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = PremiumDarkGreen,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Farm Details Module
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text("Farm Details", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Region / District") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = PremiumDarkGreen) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = PremiumDarkGreen,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("Farm Size: ${String.format("%.1f", farmSize)} Hectares", fontWeight = FontWeight.Bold, color = PremiumDarkGreen)
                Slider(
                    value = farmSize,
                    onValueChange = { farmSize = it },
                    valueRange = 0.5f..20f,
                    colors = SliderDefaults.colors(
                        thumbColor = PremiumGold,
                        activeTrackColor = PremiumDarkGreen
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Crop Portfolio Module
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text("Crop Portfolio", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
                Text("Select the crops you actively manage to customize your AI insights.", fontSize = 13.sp, color = OnSurfaceSubtle)
                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allCrops.forEach { crop ->
                        val isSelected = selectedCrops.contains(crop)
                        Surface(
                            modifier = Modifier.clickable {
                                if (isSelected) selectedCrops.remove(crop)
                                else selectedCrops.add(crop)
                            },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) PremiumDarkGreen else Color.White,
                            border = BorderStroke(1.dp, if (isSelected) PremiumDarkGreen else Color.LightGray)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = crop,
                                    color = if (isSelected) Color.White else Color.DarkGray,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Save Action
            Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                Button(
                    onClick = {
                        viewModel.updateProfile(
                            name = fullName,
                            phone = phone,
                            district = location.split(",").firstOrNull()?.trim() ?: location,
                            crops = selectedCrops.toList(),
                            farmSizeHa = farmSize.toDouble()
                        )
                        onSaveSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(12.dp, RoundedCornerShape(32.dp), spotColor = PremiumDarkGreen),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PremiumDarkGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text("Save Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
