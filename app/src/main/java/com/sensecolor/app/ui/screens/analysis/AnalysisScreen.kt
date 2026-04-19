package com.sensecolor.app.ui.screens.analysis

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.sensecolor.app.data.repository.UserPreferencesRepository
import com.sensecolor.app.ui.components.ColorLabelOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    photoUri: String,
    preferencesRepository: UserPreferencesRepository,
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val viewModel = remember { AnalysisViewModel(photoUri, preferencesRepository) }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analyze") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("\u2190", style = MaterialTheme.typography.titleLarge)
                    }
                },
                actions = {
                    if (viewModel.tapPoints.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearAllPins() }) {
                            Text("\u2715", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Text("\u2699", style = MaterialTheme.typography.titleLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Sample size chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sample:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                SampleSizeChip("S", 2, uiState.sampleRadius) { viewModel.setSampleRadius(2) }
                Spacer(modifier = Modifier.width(4.dp))
                SampleSizeChip("M", 3, uiState.sampleRadius) { viewModel.setSampleRadius(3) }
                Spacer(modifier = Modifier.width(4.dp))
                SampleSizeChip("L", 5, uiState.sampleRadius) { viewModel.setSampleRadius(5) }
            }

            // Main content area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator()
                    }
                    uiState.error != null -> {
                        Text(
                            text = uiState.error ?: "Unknown error",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(32.dp)
                        )
                    }
                    uiState.bitmap != null -> {
                        PhotoWithPins(
                            bitmap = uiState.bitmap!!,
                            tapPoints = viewModel.tapPoints,
                            selectedPointId = uiState.selectedTapPointId,
                            onTap = { normalizedX, normalizedY ->
                                viewModel.onImageTapped(normalizedX, normalizedY)
                            },
                            onPinClick = { viewModel.onPinSelected(it) }
                        )
                    }
                }
            }

            // Hint text
            if (!uiState.isLoading && uiState.error == null) {
                Text(
                    text = "Tap on the photo to identify colors",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }

    // Bottom sheet
    val selectedTapPoint = uiState.selectedTapPointId?.let { id ->
        viewModel.tapPoints.find { it.id == id }
    }
    if (selectedTapPoint != null) {
        ColorDetailSheet(
            tapPoint = selectedTapPoint,
            onDismiss = { viewModel.onDismissSheet() },
            onDelete = { viewModel.deleteTapPoint(selectedTapPoint.id) }
        )
    }
}

@Composable
private fun PhotoWithPins(
    bitmap: Bitmap,
    tapPoints: List<com.sensecolor.app.data.model.TapPoint>,
    selectedPointId: Int?,
    onTap: (Float, Float) -> Unit,
    onPinClick: (com.sensecolor.app.data.model.TapPoint) -> Unit
) {
    var viewSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { viewSize = it }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (viewSize.width == 0 || viewSize.height == 0) return@detectTapGestures

                    val bw = bitmap.width.toFloat()
                    val bh = bitmap.height.toFloat()
                    val vw = viewSize.width.toFloat()
                    val vh = viewSize.height.toFloat()

                    val imageAspect = bw / bh
                    val viewAspect = vw / vh

                    val scaledWidth: Float
                    val scaledHeight: Float
                    val offsetX: Float
                    val offsetY: Float

                    if (imageAspect > viewAspect) {
                        scaledWidth = vw
                        scaledHeight = vw / imageAspect
                        offsetX = 0f
                        offsetY = (vh - scaledHeight) / 2f
                    } else {
                        scaledHeight = vh
                        scaledWidth = vh * imageAspect
                        offsetX = (vw - scaledWidth) / 2f
                        offsetY = 0f
                    }

                    val normalizedX = (offset.x - offsetX) / scaledWidth
                    val normalizedY = (offset.y - offsetY) / scaledHeight

                    if (normalizedX in 0f..1f && normalizedY in 0f..1f) {
                        onTap(normalizedX, normalizedY)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Captured photo for color analysis",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay pins — need to match ContentScale.Fit positioning
        if (tapPoints.isNotEmpty() && viewSize.width > 0) {
            val bw = bitmap.width.toFloat()
            val bh = bitmap.height.toFloat()
            val vw = viewSize.width.toFloat()
            val vh = viewSize.height.toFloat()
            val imageAspect = bw / bh
            val viewAspect = vw / vh

            val scaledWidth: Float
            val scaledHeight: Float
            val imageOffsetX: Float
            val imageOffsetY: Float

            if (imageAspect > viewAspect) {
                scaledWidth = vw
                scaledHeight = vw / imageAspect
                imageOffsetX = 0f
                imageOffsetY = (vh - scaledHeight) / 2f
            } else {
                scaledHeight = vh
                scaledWidth = vh * imageAspect
                imageOffsetX = (vw - scaledWidth) / 2f
                imageOffsetY = 0f
            }

            Box(
                modifier = Modifier
                    .size(
                        width = with(androidx.compose.ui.platform.LocalDensity.current) { scaledWidth.toDp() },
                        height = with(androidx.compose.ui.platform.LocalDensity.current) { scaledHeight.toDp() }
                    )
                    .align(Alignment.Center)
            ) {
                ColorLabelOverlay(
                    tapPoints = tapPoints,
                    selectedPointId = selectedPointId,
                    onPinClick = onPinClick
                )
            }
        }
    }
}

@Composable
private fun SampleSizeChip(
    label: String,
    radius: Int,
    currentRadius: Int,
    onClick: () -> Unit
) {
    FilterChip(
        selected = currentRadius == radius,
        onClick = onClick,
        label = { Text(label) }
    )
}
