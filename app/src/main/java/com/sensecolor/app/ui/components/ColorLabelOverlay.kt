package com.sensecolor.app.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sensecolor.app.data.model.TapPoint

@Composable
fun ColorLabelOverlay(
    tapPoints: List<TapPoint>,
    selectedPointId: Int?,
    onPinClick: (TapPoint) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val constraintWidthPx: Float = with(density) { maxWidth.toPx() }
        val constraintHeightPx: Float = with(density) { maxHeight.toPx() }
        val pinHalfSizePx: Float = with(density) { 14.dp.toPx() }

        tapPoints.forEach { tapPoint ->
            val pixelX = tapPoint.normalizedX * constraintWidthPx - pinHalfSizePx
            val pixelY = tapPoint.normalizedY * constraintHeightPx - pinHalfSizePx

            ColorPin(
                colorResult = tapPoint.colorResult,
                isSelected = tapPoint.id == selectedPointId,
                onClick = { onPinClick(tapPoint) },
                modifier = Modifier.offset {
                    IntOffset(pixelX.toInt(), pixelY.toInt())
                }
            )
        }
    }
}
