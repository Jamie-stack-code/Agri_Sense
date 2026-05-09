package com.example.agri_sense.ui.community

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.agri_sense.ml.PestDetectionModel
import com.example.agri_sense.ui.theme.*
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PestDiagnosisScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val pestModel = remember { PestDetectionModel(context) }
    val coroutineScope = rememberCoroutineScope()
    
    var isAnalyzing by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<PestDetectionModel.DetectionResult?>(null) }
    
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    Scaffold(
        containerColor = PremiumSurface,
        topBar = {
            TopAppBar(
                title = { Text("Pest Diagnosis", color = Color.White, fontWeight = FontWeight.Bold) },
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
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "AI Plant Doctor",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Identify pests or diseases instantly by taking a photo of the affected plant part.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (result == null) {
                    // Glassmorphic Viewfinder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .shadow(12.dp, RoundedCornerShape(32.dp), spotColor = PremiumDarkGreen)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (hasCameraPermission && !isAnalyzing) {
                            AndroidView(
                                factory = { ctx ->
                                    val previewView = PreviewView(ctx).apply {
                                        scaleType = PreviewView.ScaleType.FILL_CENTER
                                    }
                                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                    cameraProviderFuture.addListener({
                                        val cameraProvider = cameraProviderFuture.get()
                                        val preview = Preview.Builder().build().also {
                                            it.surfaceProvider = previewView.surfaceProvider
                                        }
                                        try {
                                            cameraProvider.unbindAll()
                                            cameraProvider.bindToLifecycle(
                                                lifecycleOwner,
                                                CameraSelector.DEFAULT_BACK_CAMERA,
                                                preview,
                                                imageCapture
                                            )
                                        } catch (e: Exception) {
                                            Log.e("PestCam", "Binding failed", e)
                                        }
                                    }, ContextCompat.getMainExecutor(ctx))
                                    previewView
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (isAnalyzing) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = PremiumGold, strokeWidth = 4.dp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Analyzing Imagery...", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text("Camera Permission Required", color = Color.White)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    // Premium Glowing CTA
                    Button(
                        onClick = {
                            if (!isAnalyzing && hasCameraPermission) {
                                isAnalyzing = true
                                val photoFile = File(context.cacheDir, "pest_${System.currentTimeMillis()}.jpg")
                                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                                imageCapture.takePicture(
                                    outputOptions,
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                            coroutineScope.launch(kotlinx.coroutines.Dispatchers.Main) {
                                                result = pestModel.detectPest(Uri.fromFile(photoFile))
                                                isAnalyzing = false
                                            }
                                        }
                                        override fun onError(exception: ImageCaptureException) {
                                            isAnalyzing = false
                                        }
                                    }
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = PremiumGold),
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                        shape = RoundedCornerShape(32.dp),
                        enabled = !isAnalyzing && hasCameraPermission
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = if (isAnalyzing) Color.LightGray else PremiumGold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (isAnalyzing) "Processing..." else "Take Photo & Analyze",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                    
                    // Removed old LaunchedEffect simulation
                } else {
                    val detection = result!!
                    
                    // Professional Result Card
                    Card(
                        modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(24.dp), spotColor = PremiumDarkGreen),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                color = if (detection.severity >= PestDetectionModel.Severity.HIGH) Color(0xFFFFEBEE) else Color(0xFFF3E5F5),
                                shape = CircleShape,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Warning, 
                                        contentDescription = null, 
                                        modifier = Modifier.size(32.dp), 
                                        tint = if (detection.severity >= PestDetectionModel.Severity.HIGH) Color(0xFFD32F2F) else PremiumDarkGreen
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = detection.pestName, 
                                fontSize = 24.sp, 
                                fontWeight = FontWeight.Black,
                                color = PremiumDarkGreen,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = PremiumGold.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Confidence: ${(detection.confidence * 100).toInt()}% • Severity: ${detection.severity}", 
                                    color = PremiumDarkGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    PremiumInfoSection(title = "Professional Treatment Plan", content = detection.treatmentPlan)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    PremiumInfoSection(title = "Organic Alternative", content = detection.organicAlternative)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Official Sites Link
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.plantwise.org/KnowledgeBank/"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = PremiumDarkGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verify on Plantwise Official", color = PremiumDarkGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(40.dp))
                    
                    // Premium Reset Action
                    Button(
                        onClick = { result = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = PremiumDarkGreen),
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumDarkGreen),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Text(
                            text = "Analyze Another Plant",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PremiumGold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun PremiumInfoSection(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = PremiumDarkGreen)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, fontSize = 15.sp, color = Color.DarkGray, lineHeight = 22.sp)
        }
    }
}
