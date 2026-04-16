package com.sensecolor.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.sensecolor.app.data.model.TestPlate
import com.sensecolor.app.util.ColorUtils
import kotlin.random.Random

@Composable
fun IshiharaPlate(plate: TestPlate, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val canvasRadius = size.minDimension / 2f
        val centerX = size.width / 2f
        val centerY = size.height / 2f

        val random = Random(plate.dotSeed)

        // Generate ~375 dots within a circle using rejection sampling
        var dotsPlaced = 0
        val targetDots = 375

        while (dotsPlaced < targetDots) {
            val nx = random.nextFloat() * 2f - 1f
            val ny = random.nextFloat() * 2f - 1f

            // Reject if outside the plate circle
            if (nx * nx + ny * ny > 0.95f * 0.95f) continue

            val dotRadiusFraction = 0.03f + random.nextFloat() * 0.04f // 3% to 7%
            val dotRadius = dotRadiusFraction * canvasRadius

            val isForeground = isPointInDigit(nx, ny, plate.hiddenNumber)

            val hue = if (isForeground) plate.foregroundHue else plate.backgroundHue
            val saturation = 0.5f + random.nextFloat() * 0.3f  // 0.5 - 0.8
            val lightness = 0.35f + random.nextFloat() * 0.2f  // 0.35 - 0.55

            val (r, g, b) = ColorUtils.hslToRgb(hue, saturation, lightness)
            val color = Color(r / 255f, g / 255f, b / 255f)

            val px = centerX + nx * canvasRadius
            val py = centerY + ny * canvasRadius

            drawCircle(
                color = color,
                radius = dotRadius,
                center = Offset(px, py)
            )

            dotsPlaced++
        }
    }
}

/**
 * Check if a point (in normalized -1..1 coordinates) falls within the shape
 * of the given number, rendered in a 7-segment display style.
 */
private fun isPointInDigit(x: Float, y: Float, number: Int): Boolean {
    val digits = number.toString()
    if (digits.length == 1) {
        return isPointInSingleDigit(x, y, digits[0] - '0')
    }

    // Multi-digit: position side by side
    val digitCount = digits.length
    val digitWidth = 0.5f / digitCount
    val totalWidth = digitWidth * 2f * digitCount
    val startX = -totalWidth / 2f

    for (i in digits.indices) {
        val digitCenterX = startX + digitWidth + i * digitWidth * 2f
        // Scale the coordinate into the single-digit space
        val localX = (x - digitCenterX) / (digitWidth / 0.3f)
        val localY = y / 0.85f
        if (isPointInSingleDigit(localX, localY, digits[i] - '0')) {
            return true
        }
    }
    return false
}

/**
 * Checks if a point falls within a single digit (0-9) rendered in 7-segment style.
 * The digit occupies roughly -0.3..0.3 horizontally and -0.5..0.5 vertically.
 *
 * Segment layout:
 *   _a_
 *  |   |
 *  f   b
 *  |_g_|
 *  |   |
 *  e   c
 *  |_d_|
 */
private fun isPointInSingleDigit(x: Float, y: Float, digit: Int): Boolean {
    val hw = 0.28f  // half-width of horizontal segments
    val sh = 0.06f  // segment thickness (half)
    val vTop = -0.47f
    val vMid = 0.0f
    val vBot = 0.47f
    val hLeft = -0.25f
    val hRight = 0.25f

    fun inHSegment(cy: Float): Boolean =
        x in (-hw..hw) && y in (cy - sh..cy + sh)

    fun inVSegmentLeft(topY: Float, botY: Float): Boolean =
        x in (hLeft - sh..hLeft + sh) && y in (topY..botY)

    fun inVSegmentRight(topY: Float, botY: Float): Boolean =
        x in (hRight - sh..hRight + sh) && y in (topY..botY)

    // Segments: a=top, b=upper-right, c=lower-right, d=bottom, e=lower-left, f=upper-left, g=middle
    val a = { inHSegment(vTop) }
    val b = { inVSegmentRight(vTop, vMid) }
    val c = { inVSegmentRight(vMid, vBot) }
    val d = { inHSegment(vBot) }
    val e = { inVSegmentLeft(vMid, vBot) }
    val f = { inVSegmentLeft(vTop, vMid) }
    val g = { inHSegment(vMid) }

    return when (digit) {
        0 -> a() || b() || c() || d() || e() || f()
        1 -> b() || c()
        2 -> a() || b() || g() || e() || d()
        3 -> a() || b() || g() || c() || d()
        4 -> f() || g() || b() || c()
        5 -> a() || f() || g() || c() || d()
        6 -> a() || f() || g() || e() || c() || d()
        7 -> a() || b() || c()
        8 -> a() || b() || c() || d() || e() || f() || g()
        9 -> a() || b() || c() || d() || f() || g()
        else -> false
    }
}
