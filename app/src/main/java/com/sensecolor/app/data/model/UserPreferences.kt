package com.sensecolor.app.data.model

data class UserPreferences(
    val hasCompletedOnboarding: Boolean = false,
    val colorBlindnessType: ColorBlindnessType = ColorBlindnessType.NONE,
    val hasCompletedTest: Boolean = false,
    val textSizeScale: Float = 1.0f
)
