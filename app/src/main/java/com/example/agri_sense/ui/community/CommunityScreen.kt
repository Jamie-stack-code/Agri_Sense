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
import kotlinx.coroutines.launch

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
                            Text("50K+", color = PremiumGold, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
                
                Text(
                    text = if (isEnglish) "Connect with 50,000+ farmers across Africa" else "Lumikizanani ndi alimi opitilira 50,000 ku Africa",
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
                        focusedTextColor = PremiumDarkGreen,
                        unfocusedTextColor = PremiumDarkGreen
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
                
                PremiumCommunityTab(
                    title = if (isEnglish) "Pest Alerts" else "Machenjezo",
                    isSelected = selectedTab == "Pest Alerts",
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Warning
                ) { selectedTab = "Pest Alerts" }
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
                Text(
                    text = if (isEnglish) "Urgent Outbreaks" else "Zofunika Mwamsanga",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Red.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 16.dp)
                )

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    pestAlerts.forEach { pest ->
                        val bgColor = when (pest.severityLevel) {
                            "critical" -> Color(0xFFFFF0F0)
                            "high" -> Color(0xFFFFF8E1)
                            else -> Color(0xFFF1F8E9)
                        }
                        val accentColor = when (pest.severityLevel) {
                            "critical" -> Color.Red
                            "high" -> Color(0xFFFF8F00)
                            else -> PremiumDarkGreen
                        }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = bgColor)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = accentColor)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        if (isEnglish) pest.pestName else pest.pestNameChichewa.ifEmpty { pest.pestName },
                                        fontWeight = FontWeight.Bold,
                                        color = accentColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    if (isEnglish) "Outbreak in ${pest.outbreakDistricts}. Affects: ${pest.affectedCrops}"
                                    else "Kubuka mu ${pest.outbreakDistricts}. Mbewu: ${pest.affectedCrops}",
                                    fontSize = 14.sp
                                )
                                if (pest.recommendedAction.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(pest.recommendedAction, fontSize = 13.sp, color = OnSurfaceSubtle)
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
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
                            focusedBorderColor = PremiumDarkGreen,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        maxLines = 5
                    )
                    
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
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Submit for Review", color = PremiumGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PremiumDarkGreen)
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
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen)
                    ) {
                        Text(if (isEnglish) "Post" else "Tumizani")
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
