package com.sensecolor.app.domain

import android.graphics.Bitmap
import com.sensecolor.app.data.model.ColorBlindnessType
import com.sensecolor.app.data.model.ColorResult
import com.sensecolor.app.util.ColorUtils

object PhotoColorAnalyzer {

    fun analyzePoint(
        bitmap: Bitmap,
        normalizedX: Float,
        normalizedY: Float,
        sampleRadius: Int = 3,
        colorBlindnessType: ColorBlindnessType = ColorBlindnessType.NONE
    ): ColorResult {
        val pixelX = (normalizedX * bitmap.width).toInt().coerceIn(0, bitmap.width - 1)
        val pixelY = (normalizedY * bitmap.height).toInt().coerceIn(0, bitmap.height - 1)

        val (r, g, b) = ColorUtils.averagePixelRegion(bitmap, pixelX, pixelY, sampleRadius)
        val (h, s, l) = ColorUtils.rgbToHsl(r, g, b)
        val hex = ColorUtils.rgbToHex(r, g, b)
        val (primaryName, specificName) = ColorNameMapper.mapColor(r, g, b)
        val confusionMessage = ColorBlindnessEngine.checkConfusion(primaryName, colorBlindnessType)

        return ColorResult(
            primaryName = primaryName,
            specificName = specificName,
            hex = hex,
            rgb = Triple(r, g, b),
            hsl = Triple(h, s, l),
            isConfusionColor = confusionMessage != null,
            confusionMessage = confusionMessage
        )
    }
}
