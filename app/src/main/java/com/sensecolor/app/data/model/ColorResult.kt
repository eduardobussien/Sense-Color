package com.sensecolor.app.data.model

data class ColorResult(
    val primaryName: String,
    val specificName: String,
    val hex: String,
    val rgb: Triple<Int, Int, Int>,
    val hsl: Triple<Float, Float, Float>,
    val isConfusionColor: Boolean = false,
    val confusionMessage: String? = null
)
