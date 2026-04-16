package com.sensecolor.app.data.model

enum class ColorBlindnessType(val displayName: String, val description: String) {
    NONE("None", "Normal color vision"),
    PROTANOPIA("Protanopia", "Red-blind"),
    DEUTERANOPIA("Deuteranopia", "Green-blind"),
    TRITANOPIA("Tritanopia", "Blue-blind"),
    PROTANOMALY("Protanomaly", "Red-weak"),
    DEUTERANOMALY("Deuteranomaly", "Green-weak (most common)"),
    TRITANOMALY("Tritanomaly", "Blue-weak"),
    ACHROMATOPSIA("Achromatopsia", "Total color blindness")
}
