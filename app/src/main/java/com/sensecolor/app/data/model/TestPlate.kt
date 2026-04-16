package com.sensecolor.app.data.model

data class TestPlate(
    val id: Int,
    val hiddenNumber: Int,
    val answersByType: Map<ColorBlindnessType, Int?>,
    val foregroundHue: Float,
    val backgroundHue: Float,
    val dotSeed: Long
)
