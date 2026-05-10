package com.example.agri_sense.ui.community

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agri_sense.ui.theme.*
import com.example.agri_sense.ui.dashboard.BottomNavBar
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    language: String,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSoil: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToPestDiagnosis: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Community Feed") }
    var showAskExpertModal by remember { mutableStateOf(false) }
    var expertQuestion by remember { mutableStateOf("") }
    var activeCommentPostId by remember { mutableStateOf<String?>(null) }
    var userComment by remember { mutableStateOf("") }
    val isEnglish = language == "English"
    val coroutineScope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: CommunityViewModel = hiltViewModel()
    val discussions by viewModel.filteredDiscussions.collectAsState()
    val pestAlerts by viewModel.pestAlerts.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    var selectedPestAlert by remember { mutableStateOf<com.example.agri_sense.data.models.PestAlert?>(null) }
    
    val farmerCount by viewModel.farmerCount.collectAsState()
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.pickImage(uri)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavBar(
                currentRoute = "community",
                onNavigateToHome = onNavigateToHome,
                onNavigateToSoil = onNavigateToSoil,
                onNavigateToMarket = onNavigateToMarket,
                onNavigateToCommunity = {},
                onNavigateToProfile = onNavigateToProfile
            )
        },
        containerColor = PremiumSurface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Premium Hero Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PremiumDarkGreen, AgriGreenScreenBg)
                        ),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
                    .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEnglish) "Farmer Network" else "Gulu la Alimi",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.People, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(farmerCount, color = PremiumGold, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
                
                Text(
                    text = if (isEnglish) "Connect with $farmerCount farmers across Africa" else "Lumikizanani ndi alimi $farmerCount ku Africa",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 28.dp),
                    fontWeight = FontWeight.Medium
                )

                // Glassmorphic Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isEnglish) "Search discussions, pests..." else "Fufuzani mitu, tizilombo...", color = OnSurfaceSubtle) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PremiumDarkGreen) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            LaunchedEffect(searchQuery) { viewModel.setSearchQuery(searchQuery) }

            // Tabs Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PremiumCommunityTab(
                    title = if (isEnglish) "Discussions" else "Zomwe Zikuchitika",
                    isSelected = selectedTab == "Community Feed",
                    modifier = Modifier.weight(1f)
                ) { selectedTab = "Community Feed" }
                
                val unreadCount by viewModel.unreadPestCount.collectAsState()
                PremiumCommunityTab(
                    title = if (isEnglish) "Pest Alerts" else "Machenjezo",
                    isSelected = selectedTab == "Pest Alerts",
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Warning,
                    badgeCount = if (unreadCount > 0) unreadCount else 0
                ) { 
                    selectedTab = "Pest Alerts"
                    if (unreadCount > 0) viewModel.markAllAlertsAsRead()
                }
            }

            // Quick Action Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PremiumCommunityActionCard(
                    title = if (isEnglish) "AI Pest ID" else "Zidziwitso za Tizilombo",
                    subtitle = if (isEnglish) "Diagnose via photo" else "Jambulani chithunzi",
                    icon = Icons.Default.DocumentScanner,
                    iconTint = PremiumDarkGreen,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToPestDiagnosis
                )
                PremiumCommunityActionCard(
                    title = if (isEnglish) "Ask Experts" else "Funsani Akatswiri",
                    subtitle = if (isEnglish) "Get instant advice" else "Pezani uphungu",
                    icon = Icons.Default.TipsAndUpdates,
                    iconTint = PremiumGold,
                    modifier = Modifier.weight(1f),
                    onClick = { showAskExpertModal = true }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Dynamic Content Based on Tab
            if (selectedTab == "Community Feed") {
                Text(
                    text = if (isEnglish) "Recent Activity" else "Zatsopano",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PremiumDarkGreen,
                    modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 16.dp)
                )

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (discussions.isEmpty()) {
                        Text(
                            text = if (isEnglish) "No discussions yet. Be the first to ask!" else "Palibe zokambirana. Khalani oyamba kufunsa!",
                            color = OnSurfaceSubtle,
                            modifier = Modifier.padding(vertical = 24.dp)
                        )
                    }
                    discussions.forEach { disc ->
                        PremiumPostCard(
                            id = disc.id,
                            author = disc.authorName,
                            location = disc.authorDistrict,
                            time = getRelativeTime(disc.postedAt),
                            content = disc.question,
                            likes = disc.likes,
                            comments = disc.replies,
                            isExpertVerified = disc.expertAnswer.isNotEmpty(),
                            expertResponse = disc.expertAnswer.ifEmpty { null },
                            imageUrl = disc.imageUrl,
                            onLike = { viewModel.likeDiscussion(it) },
                            onComment = { activeCommentPostId = it },
                            onShare = { text ->
                                val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(android.content.Intent.EXTRA_SUBJECT, "Agri-Sense Discussion")
                                    putExtra(android.content.Intent.EXTRA_TEXT, text)
                                }
                                context.startActivity(android.content.Intent.createChooser(intent, "Share via"))
                            }
                        )
                    }
                }
            } else if (selectedTab == "Pest Alerts") {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color.Red.copy(0.1f), shape = CircleShape, modifier = Modifier.size(12.dp)) {
                             Box(modifier = Modifier.fillMaxSize().background(Color.Red, CircleShape))
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (isEnglish) "Urgent Expert Advisories" else "Machenjezo a Akatswiri",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PremiumDarkGreen
                        )
                    }

                    if (pestAlerts.isEmpty()) {
                        Text(
                            text = if (isEnglish) "No active outbreaks reported." else "Palibe machenjezo atsopano.",
                            color = OnSurfaceSubtle,
                            modifier = Modifier.padding(vertical = 24.dp)
                        )
                    }

                    pestAlerts.forEach { pest ->
                        val (bgColor, accentColor, icon) = when (pest.severityLevel.lowercase()) {
                            "critical" -> Triple(Color(0xFFFFF0F0), Color.Red, Icons.Default.NewReleases)
                            "high" -> Triple(Color(0xFFFFF8E1), Color(0xFFFF8F00), Icons.Default.Warning)
                            else -> Triple(Color(0xFFF1F8E9), PremiumDarkGreen, Icons.Default.Info)
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(28.dp)),
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(accentColor)
                                        .padding(horizontal = 20.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(icon, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            pest.severityLevel.uppercase(),
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                }
                                
                                Column(modifier = Modifier.padding(24.dp)) {
                                    Text(
                                        text = if (isEnglish) pest.pestName else pest.pestNameChichewa.ifEmpty { pest.pestName },
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PremiumDarkGreen
                                    )
                                    
                                    Spacer(Modifier.height(8.dp))
                                    
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocationOn, null, tint = PremiumGold, modifier = Modifier.size(14.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            text = pest.outbreakDistricts,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OnSurfaceSubtle
                                        )
                                    }

                                    Spacer(Modifier.height(16.dp))
                                    
                                    Surface(
                                        color = bgColor,
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Shield, null, tint = accentColor, modifier = Modifier.size(16.dp))
                                                Spacer(Modifier.width(8.dp))
                                                Text(
                                                    if (isEnglish) "Expert Guidance" else "Malangizo a Katswiri",
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 12.sp,
                                                    color = accentColor
                                                )
                                            }
                                            Spacer(Modifier.height(8.dp))
                                            Text(
                                                text = if (isEnglish) pest.description else pest.descriptionChichewa.ifEmpty { pest.description },
                                                fontSize = 14.sp,
                                                lineHeight = 20.sp,
                                                color = Color.DarkGray
                                            )
                                        }
                                    }

                                    if (pest.recommendedAction.isNotEmpty()) {
                                        Spacer(Modifier.height(16.dp))
                                        Text(
                                            if (isEnglish) "Action Required:" else "Zochita:",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = PremiumDarkGreen
                                        )
                                        Text(
                                            if (isEnglish) pest.recommendedAction else pest.recommendedActionChichewa.ifEmpty { pest.recommendedAction },
                                            fontSize = 13.sp,
                                            color = Color.Gray,
                                            lineHeight = 18.sp
                                        )
                                    }
                                    
                                    Spacer(Modifier.height(20.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            getRelativeTime(pest.reportedAt),
                                            fontSize = 11.sp,
                                            color = OnSurfaceSubtle,
                                            fontWeight = FontWeight.Medium
                                        )
                                        TextButton(onClick = { selectedPestAlert = pest }) {
                                            Text(if (isEnglish) "Read Full Advisory" else "Werengani Zonse", color = accentColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (selectedPestAlert != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedPestAlert = null },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                val pest = selectedPestAlert!!
                val accentColor = when (pest.severityLevel.lowercase()) {
                    "critical" -> Color.Red
                    "high" -> Color(0xFFFF8F00)
                    else -> PremiumDarkGreen
                }
                
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Surface(color = accentColor.copy(0.1f), shape = RoundedCornerShape(8.dp)) {
                            Text(pest.severityLevel.uppercase(), color = accentColor, fontWeight = FontWeight.Black, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                        }
                        IconButton(onClick = { selectedPestAlert = null }) {
                            Icon(Icons.Default.Close, null, tint = Color.Gray)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = if (isEnglish) pest.pestName else pest.pestNameChichewa.ifEmpty { pest.pestName },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = PremiumDarkGreen,
                        lineHeight = 34.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, null, tint = OnSurfaceSubtle, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Published ${getRelativeTime(pest.reportedAt)}", fontSize = 12.sp, color = OnSurfaceSubtle)
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        if (isEnglish) "INTELLIGENCE REPORT" else "LIPOTI LA KATSWIRI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = OnSurfaceSubtle
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isEnglish) pest.description else pest.descriptionChichewa.ifEmpty { pest.description },
                        fontSize = 16.sp,
                        lineHeight = 26.sp,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        if (isEnglish) "OUTBREAK DETAILS" else "ZAMBIRI ZA KUBUKA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = OnSurfaceSubtle
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(color = PremiumSurface, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            DetailItem(Icons.Default.LocationOn, if (isEnglish) "Districts" else "Zigawo", pest.outbreakDistricts)
                            DetailItem(Icons.Default.Agriculture, if (isEnglish) "Affected Crops" else "Mbewu", pest.affectedCrops)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        if (isEnglish) "EXPERT RECOMMENDED ACTIONS" else "ZOCHITA ZOTSIMIKIZIDWA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = accentColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(color = accentColor.copy(0.05f), border = BorderStroke(1.dp, accentColor.copy(0.2f)), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (isEnglish) pest.recommendedAction else pest.recommendedActionChichewa.ifEmpty { pest.recommendedAction },
                            modifier = Modifier.padding(20.dp),
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    Button(
                        onClick = { selectedPestAlert = null },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(if (isEnglish) "I Understand" else "Ndizimvetsera", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        if (showAskExpertModal) {
            ModalBottomSheet(
                onDismissRequest = { showAskExpertModal = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (isEnglish) "Ask an Agronomist" else "Funsani Katswiri",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = PremiumDarkGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        if (isEnglish) "Professional network experts are ready to assist you." else "Akatswiri athu akukonzekera kukuthandizani.",
                        color = OnSurfaceSubtle,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    OutlinedTextField(
                        value = expertQuestion,
                        onValueChange = { expertQuestion = it },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        placeholder = { Text(if (isEnglish) "Describe your farm issue in detail..." else "Fotokozani vuto lanu...") },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = PremiumDarkGreen,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        maxLines = 5
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Image Picker & Preview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = PremiumDarkGreen.copy(alpha = 0.1f))
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Attach Photo", tint = PremiumDarkGreen)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            if (selectedImageUri != null) "Photo attached" else "Attach farm photo",
                            color = if (selectedImageUri != null) PremiumDarkGreen else OnSurfaceSubtle,
                            fontWeight = if (selectedImageUri != null) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    
                    if (selectedImageUri != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp))) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { viewModel.clearSelectedImage() },
                                modifier = Modifier.align(Alignment.TopEnd).size(24.dp).padding(4.dp),
                                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            if (expertQuestion.isNotBlank()) {
                                viewModel.postQuestion(expertQuestion)
                                coroutineScope.launch {
                                    showAskExpertModal = false
                                    expertQuestion = ""
                                    snackbarHostState.showSnackbar(if (isEnglish) "Question posted to community!" else "Funso lathunzidwa!")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PremiumDarkGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Submit for Review", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        if (activeCommentPostId != null) {
            AlertDialog(
                onDismissRequest = { activeCommentPostId = null },
                containerColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                title = { Text(if (isEnglish) "Add Comment" else "Yankhani", fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen) },
                text = {
                    OutlinedTextField(
                        value = userComment,
                        onValueChange = { userComment = it },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        placeholder = { Text(if (isEnglish) "Write your response..." else "Lembani yankho lanu...") },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = PremiumDarkGreen
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (userComment.isNotBlank()) {
                                viewModel.addComment(activeCommentPostId!!, userComment)
                                activeCommentPostId = null
                                userComment = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PremiumDarkGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text(if (isEnglish) "Post" else "Tumizani", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { activeCommentPostId = null }) {
                        Text(if (isEnglish) "Cancel" else "Letsani", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
fun PremiumCommunityTab(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) PremiumDarkGreen else Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (isSelected) 8.dp else 2.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) PremiumGold else Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.Gray
            )
            
            if (badgeCount > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    color = Color.Red,
                    shape = CircleShape,
                    modifier = Modifier.size(18.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = badgeCount.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumCommunityActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick).shadow(8.dp, RoundedCornerShape(24.dp), spotColor = iconTint),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = iconTint.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(28.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = PremiumDarkGreen, textAlign = TextAlign.Center)
            Text(text = subtitle, fontSize = 12.sp, color = OnSurfaceSubtle, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun PremiumPostCard(
    id: String,
    author: String,
    location: String,
    time: String,
    content: String,
    likes: Int,
    comments: Int,
    isExpertVerified: Boolean = false,
    expertResponse: String? = null,
    imageUrl: String = "",
    onLike: (String) -> Unit,
    onComment: (String) -> Unit,
    onShare: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = PremiumDarkGreen.copy(alpha = 0.1f), shape = CircleShape, modifier = Modifier.size(48.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text("👨‍🌾", fontSize = 24.sp) }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = author, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = PremiumDarkGreen)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "$location • $time", color = OnSurfaceSubtle, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = content, color = Color.DarkGray, fontSize = 15.sp, lineHeight = 22.sp)
            
            if (imageUrl.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Farm Image",
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            if (isExpertVerified && expertResponse != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = PremiumDarkGreen.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, PremiumGold.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Expert Verified Response", fontWeight = FontWeight.Bold, color = PremiumDarkGreen, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = expertResponse, color = Color.DarkGray, fontSize = 14.sp, lineHeight = 20.sp)
                    }
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF0F0F0))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                // Like Button
                Row(
                    modifier = Modifier.clickable { onLike(id) }.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.ThumbUp, contentDescription = null, tint = PremiumDarkGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("$likes", color = PremiumDarkGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                
                // Comment Button
                Row(
                    modifier = Modifier.clickable { onComment(id) }.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("$comments", color = OnSurfaceSubtle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                
                // Share Button
                Row(
                    modifier = Modifier.clickable { onShare("Check out this discussion on Agri-Sense: \"$content\" - by $author") }.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Share, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share", color = OnSurfaceSubtle, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
            }
        }
    }
}

private fun getRelativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val minutes = diff / 60_000
    val hours = minutes / 60
    val days = hours / 24
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        else -> "${days / 7}w ago"
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = PremiumDarkGreen, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Black, color = OnSurfaceSubtle, letterSpacing = 1.sp)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PremiumDarkGreen)
        }
    }
}
