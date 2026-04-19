package com.sensecolor.app.ui.screens.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sensecolor.app.data.model.TapPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorDetailSheet(
    tapPoint: TapPoint,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val colorResult = tapPoint.colorResult
    val fillColor = Color(
        red = colorResult.rgb.first / 255f,
        green = colorResult.rgb.second / 255f,
        blue = colorResult.rgb.third / 255f
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            // Color swatch
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(fillColor, MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tier 2 name (specific)
            Text(
                text = colorResult.specificName,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Tier 1 name (primary)
            Text(
                text = colorResult.primaryName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            // Info rows
            InfoRow(label = "Hex", value = colorResult.hex)
            InfoRow(
                label = "RGB",
                value = "R: ${colorResult.rgb.first}, G: ${colorResult.rgb.second}, B: ${colorResult.rgb.third}"
            )
            InfoRow(
                label = "HSL",
                value = "H: ${colorResult.hsl.first.toInt()}\u00B0, S: ${(colorResult.hsl.second * 100).toInt()}%, L: ${(colorResult.hsl.third * 100).toInt()}%"
            )

            // Confusion warning
            if (colorResult.isConfusionColor && colorResult.confusionMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = colorResult.confusionMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF8D6E00),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Remove pin button
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Remove this pin")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.25f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.75f)
        )
    }
}
