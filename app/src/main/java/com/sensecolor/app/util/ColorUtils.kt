package com.sensecolor.app.util

import android.graphics.Bitmap
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

object ColorUtils {

    /**
     * Convert RGB (0-255) to HSL (h: 0-360, s: 0-1, l: 0-1).
     * For achromatic colors (grays, black, white), hue is 0.
     */
    fun rgbToHsl(r: Int, g: Int, b: Int): Triple<Float, Float, Float> {
        val rf = r / 255f
        val gf = g / 255f
        val bf = b / 255f

        val max = max(rf, max(gf, bf))
        val min = min(rf, min(gf, bf))
        val delta = max - min

        val l = (max + min) / 2f

        if (delta == 0f) {
            // Achromatic (gray, black, white)
            return Triple(0f, 0f, l)
        }

        val s = if (l <= 0.5f) {
            delta / (max + min)
        } else {
            delta / (2f - max - min)
        }

        val h = when (max) {
            rf -> {
                var hue = ((gf - bf) / delta) % 6f
                if (hue < 0f) hue += 6f
                hue * 60f
            }
            gf -> ((bf - rf) / delta + 2f) * 60f
            else -> ((rf - gf) / delta + 4f) * 60f
        }

        return Triple(h, s, l)
    }

    /**
     * Convert HSL (h: 0-360, s: 0-1, l: 0-1) back to RGB (0-255).
     */
    fun hslToRgb(h: Float, s: Float, l: Float): Triple<Int, Int, Int> {
        if (s == 0f) {
            // Achromatic
            val v = (l * 255f).roundToInt().coerceIn(0, 255)
            return Triple(v, v, v)
        }

        val q = if (l < 0.5f) l * (1f + s) else l + s - l * s
        val p = 2f * l - q

        val hNorm = h / 360f

        val r = hueToRgbChannel(p, q, hNorm + 1f / 3f)
        val g = hueToRgbChannel(p, q, hNorm)
        val b = hueToRgbChannel(p, q, hNorm - 1f / 3f)

        return Triple(
            (r * 255f).roundToInt().coerceIn(0, 255),
            (g * 255f).roundToInt().coerceIn(0, 255),
            (b * 255f).roundToInt().coerceIn(0, 255)
        )
    }

    private fun hueToRgbChannel(p: Float, q: Float, tInput: Float): Float {
        var t = tInput
        if (t < 0f) t += 1f
        if (t > 1f) t -= 1f
        return when {
            t < 1f / 6f -> p + (q - p) * 6f * t
            t < 1f / 2f -> q
            t < 2f / 3f -> p + (q - p) * (2f / 3f - t) * 6f
            else -> p
        }
    }

    /**
     * Format RGB values as a hex color string "#RRGGBB".
     */
    fun rgbToHex(r: Int, g: Int, b: Int): String {
        return "#%02X%02X%02X".format(r, g, b)
    }

    /**
     * Weighted HSL distance.
     * Hue weight 3x, saturation 1x, lightness 1.5x.
     * Hue wraps around 360 so distance between 10 and 350 is 20, not 340.
     * Hue difference is normalized to 0-180 before weighting.
     */
    fun hslDistance(
        h1: Float, s1: Float, l1: Float,
        h2: Float, s2: Float, l2: Float
    ): Float {
        // Hue difference wrapping around 360, normalized to 0-180
        var hueDiff = abs(h1 - h2) % 360f
        if (hueDiff > 180f) hueDiff = 360f - hueDiff

        // Normalize hue difference to 0-1 range (180 max -> 1.0)
        val hueNorm = hueDiff / 180f

        val sDiff = abs(s1 - s2)
        val lDiff = abs(l1 - l2)

        val weightedH = 3f * hueNorm
        val weightedS = 1f * sDiff
        val weightedL = 1.5f * lDiff

        return sqrt(weightedH * weightedH + weightedS * weightedS + weightedL * weightedL)
    }

    /**
     * Sample a square region of (2*radius+1)^2 pixels around centerX, centerY from a Bitmap.
     * Clamps to bitmap bounds. Returns average RGB.
     */
    fun averagePixelRegion(
        bitmap: Bitmap,
        centerX: Int,
        centerY: Int,
        radius: Int
    ): Triple<Int, Int, Int> {
        val startX = max(0, centerX - radius)
        val endX = min(bitmap.width - 1, centerX + radius)
        val startY = max(0, centerY - radius)
        val endY = min(bitmap.height - 1, centerY + radius)

        var rSum = 0L
        var gSum = 0L
        var bSum = 0L
        var count = 0

        for (y in startY..endY) {
            for (x in startX..endX) {
                val pixel = bitmap.getPixel(x, y)
                rSum += android.graphics.Color.red(pixel)
                gSum += android.graphics.Color.green(pixel)
                bSum += android.graphics.Color.blue(pixel)
                count++
            }
        }

        if (count == 0) {
            return Triple(0, 0, 0)
        }

        return Triple(
            (rSum / count).toInt(),
            (gSum / count).toInt(),
            (bSum / count).toInt()
        )
    }
}
