package com.sensecolor.app.ui.screens.camera

import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.sensecolor.app.util.RequireCameraPermission

@Composable
fun CameraScreen(
    onPhotoTaken: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    RequireCameraPermission(
        onGranted = {
            CameraContent(
                onPhotoTaken = onPhotoTaken,
                onNavigateToSettings = onNavigateToSettings
            )
        }
    )
}

@Composable
private fun CameraContent(
    onPhotoTaken: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraViewModel = remember { CameraViewModel() }
    val uiState by cameraViewModel.uiState.collectAsState()
    val imageCapture = remember { ImageCapture.Builder().build() }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    // Rebind camera when previewView is available or when front/back is toggled
    LaunchedEffect(previewView, uiState.useFrontCamera) {
        val view = previewView ?: return@LaunchedEffect
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.surfaceProvider = view.surfaceProvider
        }
        val cameraSelector = if (uiState.useFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (_: Exception) {
            // Camera binding failed
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera preview
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    previewView = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Bottom gradient overlay — transparent to dark, covering bottom 30%
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.30f)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC000000))
                    )
                )
        )

        // Settings button (top right) with semi-transparent pill background
        Text(
            text = "Settings",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color(0x88000000), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable(onClick = onNavigateToSettings)
        )

        // Bottom controls: flip button + capture button
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Camera flip button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.40f), CircleShape)
                    .clickable { cameraViewModel.toggleCamera() }
                    .semantics { contentDescription = "Switch camera" }
            ) {
                Text(
                    text = "⇄",
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Capture button
            if (uiState.isCapturing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(72.dp),
                    color = Color.White
                )
            } else {
                CaptureButton(
                    onClick = {
                        cameraViewModel.capturePhoto(context, imageCapture, onPhotoTaken)
                    }
                )
            }
        }

        // Error snackbar
        uiState.error?.let { errorMessage ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { cameraViewModel.clearError() }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text(text = errorMessage)
            }
        }
    }
}

@Composable
private fun CaptureButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Color.White, CircleShape)
            .border(BorderStroke(4.dp, Color.DarkGray), CircleShape)
            .clickable(onClick = onClick)
            .semantics { contentDescription = "Take photo" }
    )
}
