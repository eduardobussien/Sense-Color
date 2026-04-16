package com.sensecolor.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sensecolor.app.data.model.ColorResult

@Composable
fun ColorPin(
    colorResult: ColorResult,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pinColor = Color(
        red = colorResult.rgb.first / 255f,
        green = colorResult.rgb.second / 255f,
        blue = colorResult.rgb.third / 255f
    )
    val borderWidth = if (isSelected) 3.dp else 2.dp
    val elevation = if (isSelected) 8.dp else 0.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .shadow(elevation, CircleShape)
                .background(pinColor, CircleShape)
                .border(borderWidth, Color.White, CircleShape)
        )
        Text(
            text = colorResult.primaryName,
            style = TextStyle(
                fontSize = 8.sp,
                color = Color.White,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(1f, 1f),
                    blurRadius = 2f
                ),
                textAlign = TextAlign.Center
            )
        )
    }
}
