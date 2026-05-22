package com.sensecolor.app.domain

import com.sensecolor.app.data.model.ColorBlindnessType

object ColorBlindnessEngine {

    private data class ConfusionPair(
        val color1: String,
        val color2: String,
        val message: String
    )

    private val confusionMap: Map<ColorBlindnessType, List<ConfusionPair>> = mapOf(
        ColorBlindnessType.PROTANOPIA to listOf(
            ConfusionPair("Red", "Green", "You might see this as green — it's actually Red"),
            ConfusionPair("Green", "Red", "You might see this as red — it's actually Green"),
            ConfusionPair("Red", "Brown", "You might see this as brown — it's actually Red"),
            ConfusionPair("Brown", "Red", "You might see this as red — it's actually Brown"),
            ConfusionPair("Green", "Brown", "You might see this as brown — it's actually Green"),
            ConfusionPair("Brown", "Green", "You might see this as green — it's actually Brown"),
            ConfusionPair("Orange", "Green", "You might see this as green — it's actually Orange"),
            ConfusionPair("Blue", "Purple", "You might confuse this with purple — it's Blue"),
            ConfusionPair("Purple", "Blue", "You might confuse this with blue — it's Purple")
        ),
        ColorBlindnessType.DEUTERANOPIA to listOf(
            ConfusionPair("Red", "Green", "You might see this as green — it's actually Red"),
            ConfusionPair("Green", "Red", "You might see this as red — it's actually Green"),
            ConfusionPair("Red", "Brown", "You might see this as brown — it's actually Red"),
            ConfusionPair("Brown", "Red", "You might see this as red — it's actually Brown"),
            ConfusionPair("Green", "Brown", "You might see this as brown — it's actually Green"),
            ConfusionPair("Brown", "Green", "You might see this as green — it's actually Brown"),
            ConfusionPair("Orange", "Yellow", "You might confuse this with yellow — it's Orange"),
            ConfusionPair("Blue", "Purple", "You might confuse this with purple — it's Blue"),
            ConfusionPair("Purple", "Blue", "You might confuse this with blue — it's Purple")
        ),
        ColorBlindnessType.TRITANOPIA to listOf(
            ConfusionPair("Blue", "Green", "You might see this as green — it's actually Blue"),
            ConfusionPair("Green", "Blue", "You might see this as blue — it's actually Green"),
            ConfusionPair("Yellow", "Pink", "You might see this as pink — it's actually Yellow"),
            ConfusionPair("Pink", "Yellow", "You might see this as yellow — it's actually Pink"),
            ConfusionPair("Purple", "Red", "You might see this as red — it's actually Purple"),
            ConfusionPair("Orange", "Pink", "You might confuse this with pink — it's Orange")
        ),
        ColorBlindnessType.PROTANOMALY to listOf(
            ConfusionPair("Red", "Green", "You might have difficulty distinguishing this Red from green"),
            ConfusionPair("Green", "Red", "You might have difficulty distinguishing this Green from red"),
            ConfusionPair("Red", "Brown", "This Red may appear brownish to you"),
            ConfusionPair("Orange", "Yellow", "This Orange may appear more yellow to you")
        ),
        ColorBlindnessType.DEUTERANOMALY to listOf(
            ConfusionPair("Red", "Green", "You might have difficulty distinguishing this Red from green"),
            ConfusionPair("Green", "Red", "You might have difficulty distinguishing this Green from red"),
            ConfusionPair("Green", "Brown", "This Green may appear brownish to you"),
            ConfusionPair("Orange", "Yellow", "This Orange may appear more yellow to you")
        ),
        ColorBlindnessType.TRITANOMALY to listOf(
            ConfusionPair("Blue", "Green", "You might have difficulty distinguishing this Blue from green"),
            ConfusionPair("Yellow", "Pink", "This Yellow may appear pinkish to you")
        ),
        ColorBlindnessType.ACHROMATOPSIA to listOf(
            ConfusionPair("Red", "Gray", "This is Red — you may perceive it as a shade of gray"),
            ConfusionPair("Green", "Gray", "This is Green — you may perceive it as a shade of gray"),
            ConfusionPair("Blue", "Gray", "This is Blue — you may perceive it as a shade of gray"),
            ConfusionPair("Yellow", "Gray", "This is Yellow — you may perceive it as a shade of gray"),
            ConfusionPair("Orange", "Gray", "This is Orange — you may perceive it as a shade of gray"),
            ConfusionPair("Purple", "Gray", "This is Purple — you may perceive it as a shade of gray"),
            ConfusionPair("Pink", "Gray", "This is Pink — you may perceive it as a shade of gray"),
            ConfusionPair("Teal", "Gray", "This is Teal — you may perceive it as a shade of gray"),
            ConfusionPair("Cyan", "Gray", "This is Cyan — you may perceive it as a shade of gray"),
            ConfusionPair("Magenta", "Gray", "This is Magenta — you may perceive it as a shade of gray")
        )
    )

    fun checkConfusion(tier1Name: String, type: ColorBlindnessType): String? {
        if (type == ColorBlindnessType.NONE) return null
        val pairs = confusionMap[type] ?: return null
        return pairs.firstOrNull { it.color1 == tier1Name }?.message
    }

    /**
     * Returns a 20-element ColorMatrix float array (4×5 row-major, RGBA+offset) for the given
     * color blindness type, suitable for use with android.graphics.ColorMatrix(FloatArray).
     * Returns null for NONE (no transformation needed).
     *
     * Matrices based on Machado et al. 2009, severity = 1.0.
     * Anomaly types (protanomaly, deuteranomaly, tritanomaly) use 50% blend with identity.
     */
    fun getSimulationMatrix(type: ColorBlindnessType): FloatArray? {
        // Convert a 3×3 RGB matrix (row-major) to a 20-element ColorMatrix (4×5)
        fun to20(m: FloatArray): FloatArray = floatArrayOf(
            m[0], m[1], m[2], 0f, 0f,   // R' = f(R,G,B)
            m[3], m[4], m[5], 0f, 0f,   // G' = f(R,G,B)
            m[6], m[7], m[8], 0f, 0f,   // B' = f(R,G,B)
            0f,   0f,   0f,   1f, 0f    // A passthrough
        )

        // Blend a 3×3 matrix 50% with the identity matrix
        fun blend50(m: FloatArray): FloatArray {
            val identity = floatArrayOf(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f)
            return FloatArray(9) { i -> (m[i] + identity[i]) / 2f }
        }

        val protanopia = floatArrayOf(
             0.152286f,  1.052583f, -0.204868f,
             0.114503f,  0.786281f,  0.099216f,
            -0.003882f, -0.048116f,  1.051998f
        )
        val deuteranopia = floatArrayOf(
             0.367322f,  0.860646f, -0.227968f,
             0.280085f,  0.672501f,  0.047413f,
            -0.011820f,  0.042940f,  0.968881f
        )
        val tritanopia = floatArrayOf(
             1.255528f, -0.076749f, -0.178779f,
            -0.078411f,  0.930809f,  0.147602f,
             0.004733f,  0.691367f,  0.303900f
        )
        val achromatopsia = floatArrayOf(
            0.299f, 0.587f, 0.114f,
            0.299f, 0.587f, 0.114f,
            0.299f, 0.587f, 0.114f
        )

        return when (type) {
            ColorBlindnessType.NONE         -> null
            ColorBlindnessType.PROTANOPIA   -> to20(protanopia)
            ColorBlindnessType.DEUTERANOPIA -> to20(deuteranopia)
            ColorBlindnessType.TRITANOPIA   -> to20(tritanopia)
            ColorBlindnessType.PROTANOMALY  -> to20(blend50(protanopia))
            ColorBlindnessType.DEUTERANOMALY -> to20(blend50(deuteranopia))
            ColorBlindnessType.TRITANOMALY  -> to20(blend50(tritanopia))
            ColorBlindnessType.ACHROMATOPSIA -> to20(achromatopsia)
        }
    }
}
