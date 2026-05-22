package com.sensecolor.app.ui.screens.analysis

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.sensecolor.app.data.model.ColorResult
import com.sensecolor.app.data.model.TapPoint
import java.io.File
import java.io.FileOutputStream

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
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

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
            InfoRow(
                label = "Hex",
                value = colorResult.hex,
                onCopyHex = {
                    clipboardManager.setText(AnnotatedString(colorResult.hex))
                }
            )
            InfoRow(
                label = "RGB",
                value = "R: ${colorResult.rgb.first}, G: ${colorResult.rgb.second}, B: ${colorResult.rgb.third}"
            )
            InfoRow(
                label = "HSL",
                value = "H: ${colorResult.hsl.first.toInt()}°, S: ${(colorResult.hsl.second * 100).toInt()}%, L: ${(colorResult.hsl.third * 100).toInt()}%"
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

            // Share color card button
            Button(
                onClick = { shareColorCard(context, colorResult) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Share color card")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Remove pin button
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Remove color pin from photo" }
            ) {
                Text("Remove this pin")
            }
        }
    }
}

private fun shareColorCard(context: Context, colorResult: ColorResult) {
    val width = 900
    val height = 420
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Background: white
    val bgPaint = Paint().apply { color = android.graphics.Color.WHITE }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    // Left panel: solid color rectangle
    val r = colorResult.rgb.first
    val g = colorResult.rgb.second
    val b = colorResult.rgb.third
    val panelColor = android.graphics.Color.rgb(r, g, b)
    val colorPaint = Paint().apply { color = panelColor }
    canvas.drawRect(0f, 0f, 420f, height.toFloat(), colorPaint)

    // Thin vertical divider at x=420
    val dividerPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#E0E0E0")
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }
    canvas.drawLine(420f, 0f, 420f, height.toFloat(), dividerPaint)

    // Right panel text
    val darkColor = android.graphics.Color.parseColor("#1C1208")
    val mutedColor = android.graphics.Color.parseColor("#4A3F35")
    val brandColor = android.graphics.Color.parseColor("#9E9E9E")

    val textXLabel = 452f
    val textXValue = 580f

    // Color name (specificName) — bold, size 42
    val namePaint = Paint().apply {
        color = darkColor
        textSize = 42f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }
    canvas.drawText(colorResult.specificName, textXLabel, 60f, namePaint)

    // Primary category — normal, size 28
    val categoryPaint = Paint().apply {
        color = mutedColor
        textSize = 28f
        typeface = Typeface.DEFAULT
        isAntiAlias = true
    }
    canvas.drawText(colorResult.primaryName, textXLabel, 115f, categoryPaint)

    // Label paint — normal, size 22, muted
    val labelPaint = Paint().apply {
        color = mutedColor
        textSize = 22f
        typeface = Typeface.DEFAULT
        isAntiAlias = true
    }

    // Value paint — bold, size 22, dark
    val valuePaint = Paint().apply {
        color = darkColor
        textSize = 22f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }

    // Hex row at y=175
    canvas.drawText("Hex", textXLabel, 175f, labelPaint)
    canvas.drawText(colorResult.hex, textXValue, 175f, valuePaint)

    // RGB row at y=220
    val rgbValue = "R: $r, G: $g, B: $b"
    canvas.drawText("RGB", textXLabel, 220f, labelPaint)
    canvas.drawText(rgbValue, textXValue, 220f, valuePaint)

    // HSL row at y=265
    val h = colorResult.hsl.first.toInt()
    val s = (colorResult.hsl.second * 100).toInt()
    val l = (colorResult.hsl.third * 100).toInt()
    val hslValue = "H: $h°, S: $s%, L: $l%"
    canvas.drawText("HSL", textXLabel, 265f, labelPaint)
    canvas.drawText(hslValue, textXValue, 265f, valuePaint)

    // Branding text — centered in right panel (x center = 660), y=330
    val brandPaint = Paint().apply {
        color = brandColor
        textSize = 18f
        typeface = Typeface.DEFAULT
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("🎨 Sense Color", 660f, 330f, brandPaint)

    // Save bitmap to cache
    val sharedImagesDir = File(context.cacheDir, "shared_images")
    sharedImagesDir.mkdirs()
    val file = File(sharedImagesDir, "color_card.png")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    // Get URI via FileProvider
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    // Build and launch share intent
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share color card"))
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    onCopyHex: (() -> Unit)? = null
) {
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
        if (onCopyHex != null) {
            Column(modifier = Modifier.weight(0.75f)) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.clickable(onClick = onCopyHex)
                )
                Text(
                    text = "(tap to copy)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(0.75f)
            )
        }
    }
}
