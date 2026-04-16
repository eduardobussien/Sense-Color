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
}
