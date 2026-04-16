package com.sensecolor.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Creates a scaled version of the typography for accessibility text size preference.
 */
fun scaledTypography(scale: Float): Typography {
    return Typography(
        displayLarge = Typography.displayLarge.copy(fontSize = (34 * scale).sp, lineHeight = (40 * scale).sp),
        headlineLarge = Typography.headlineLarge.copy(fontSize = (28 * scale).sp, lineHeight = (36 * scale).sp),
        headlineMedium = Typography.headlineMedium.copy(fontSize = (24 * scale).sp, lineHeight = (32 * scale).sp),
        titleLarge = Typography.titleLarge.copy(fontSize = (22 * scale).sp, lineHeight = (28 * scale).sp),
        titleMedium = Typography.titleMedium.copy(fontSize = (16 * scale).sp, lineHeight = (24 * scale).sp),
        bodyLarge = Typography.bodyLarge.copy(fontSize = (16 * scale).sp, lineHeight = (24 * scale).sp),
        bodyMedium = Typography.bodyMedium.copy(fontSize = (14 * scale).sp, lineHeight = (20 * scale).sp),
        bodySmall = Typography.bodySmall.copy(fontSize = (12 * scale).sp, lineHeight = (16 * scale).sp),
        labelLarge = Typography.labelLarge.copy(fontSize = (14 * scale).sp, lineHeight = (20 * scale).sp),
        labelSmall = Typography.labelSmall.copy(fontSize = (11 * scale).sp, lineHeight = (16 * scale).sp)
    )
}
