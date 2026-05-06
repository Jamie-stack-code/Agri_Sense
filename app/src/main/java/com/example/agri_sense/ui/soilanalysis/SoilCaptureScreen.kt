package com.example.agri_sense.ui.soilanalysis

import android.Manifest
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.agri_sense.ui.theme.*
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoilCaptureScreen(
    viewModel: SoilViewModel,
    onPhotosCaptured: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var captureStep by remember { mutableStateOf(1) } // 1: Dry, 2: Wet
    var isCapturing by remember { mutableStateOf(false) }
    var flashEnabled by remember { mutableStateOf(false) }
    // Holds the URI of the captured dry-soil photo so we can pass both to runAnalysis
    var dryPhotoUri by remember { mutableStateOf<Uri?>(null) }
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

    // CameraX setup
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    // Pulsing shutter effect
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (captureStep == 1) "Capture Dry Soil" else "Capture Wet Soil",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {
                    IconButton(onClick = { flashEnabled = !flashEnabled }) {
                        Icon(
                            if (flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            contentDescription = "Toggle Flash",
                            tint = if (flashEnabled) PremiumGold else Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Camera Preview
            if (hasCameraPermission) {
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
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            try {
                                cameraProvider.unbindAll()
                                val camera = cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageCapture
                                )
                                // Flash mode
                                if (camera.cameraInfo.hasFlashUnit()) {
                                    imageCapture.flashMode = if (flashEnabled)
                                        ImageCapture.FLASH_MODE_ON
                                    else
                                        ImageCapture.FLASH_MODE_OFF
                                }
                            } catch (e: Exception) {
                                Log.e("SoilCapture", "Camera binding failed", e)
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                        previewView
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(32.dp))
                )
            } else {
                // Fallback when no camera permission
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color.DarkGray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Camera permission required.\nPlease grant camera access in Settings.",
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                }
            }

            // Focus Frame Overlay
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(280.dp)
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (captureStep == 1) "Position dry soil\nin center frame" else "Position dampened soil\nin center frame",
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            // Capture Controls
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                        )
                    )
                    .padding(bottom = 56.dp, top = 32.dp, start = 32.dp, end = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = PremiumGold,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Text(
                        text = if (captureStep == 1) "STEP 1: DRY SAMPLE" else "STEP 2: WET SAMPLE",
                        color = PremiumDarkGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        letterSpacing = 1.sp
                    )
                }

                // Shutter Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(96.dp)
                        .scale(if (!isCapturing) pulseScale else 1f)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable(enabled = !isCapturing) {
                            isCapturing = true
                            // Capture real photo
                            val photoFile = File(
                                context.cacheDir,
                                "soil_${if (captureStep == 1) "dry" else "wet"}_${System.currentTimeMillis()}.jpg"
                            )
                            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                            imageCapture.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                        Log.d("SoilCapture", "Photo saved: ${photoFile.absolutePath}")
                                        if (captureStep == 1) {
                                            // Store dry URI and move to wet-soil step
                                            dryPhotoUri = Uri.fromFile(photoFile)
                                            captureStep = 2
                                            isCapturing = false
                                        } else {
                                            // Both photos done — kick off analysis in ViewModel
                                            val wetUri = Uri.fromFile(photoFile)
                                            viewModel.runAnalysis(
                                                dryPhotoUri ?: wetUri,
                                                wetUri
                                            )
                                            onPhotosCaptured()
                                        }
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        Log.e("SoilCapture", "Photo capture failed", exception)
                                        // Fallback: use a dummy URI so the app doesn't crash
                                        if (captureStep == 1) {
                                            dryPhotoUri = Uri.fromFile(photoFile)
                                            captureStep = 2
                                            isCapturing = false
                                        } else {
                                            viewModel.runAnalysis(
                                                dryPhotoUri ?: Uri.fromFile(photoFile),
                                                Uri.fromFile(photoFile)
                                            )
                                            onPhotosCaptured()
                                        }
                                    }
                                }
                            )
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(if (isCapturing) PremiumDarkGreen else Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Ensure the soil sample is clearly visible in good lighting.",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Flash effect on capture
            if (isCapturing && flashEnabled) {
                Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.8f)))
            }
        }
    }
}
