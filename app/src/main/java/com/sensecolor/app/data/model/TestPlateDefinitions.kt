package com.sensecolor.app.data.model

val defaultTestPlates: List<TestPlate> = listOf(
    // Plates 1-4: Red-green confusion (foreground ~0-15 hue / background ~120 hue)
    // Test protan/deutan
    TestPlate(
        id = 1,
        hiddenNumber = 8,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 3,
            ColorBlindnessType.DEUTERANOPIA to 3,
            ColorBlindnessType.PROTANOMALY to 3,
            ColorBlindnessType.DEUTERANOMALY to null,
            ColorBlindnessType.TRITANOPIA to 8,
            ColorBlindnessType.TRITANOMALY to 8,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 5f,
        backgroundHue = 120f,
        dotSeed = 1001L
    ),
    TestPlate(
        id = 2,
        hiddenNumber = 5,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 2,
            ColorBlindnessType.DEUTERANOPIA to 2,
            ColorBlindnessType.PROTANOMALY to 2,
            ColorBlindnessType.DEUTERANOMALY to null,
            ColorBlindnessType.TRITANOPIA to 5,
            ColorBlindnessType.TRITANOMALY to 5,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 10f,
        backgroundHue = 125f,
        dotSeed = 1002L
    ),
    TestPlate(
        id = 3,
        hiddenNumber = 6,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to null,
            ColorBlindnessType.DEUTERANOPIA to null,
            ColorBlindnessType.PROTANOMALY to null,
            ColorBlindnessType.DEUTERANOMALY to null,
            ColorBlindnessType.TRITANOPIA to 6,
            ColorBlindnessType.TRITANOMALY to 6,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 0f,
        backgroundHue = 118f,
        dotSeed = 1003L
    ),
    TestPlate(
        id = 4,
        hiddenNumber = 12,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 17,
            ColorBlindnessType.DEUTERANOPIA to 17,
            ColorBlindnessType.PROTANOMALY to 17,
            ColorBlindnessType.DEUTERANOMALY to 17,
            ColorBlindnessType.TRITANOPIA to 12,
            ColorBlindnessType.TRITANOMALY to 12,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 15f,
        backgroundHue = 122f,
        dotSeed = 1004L
    ),
    // Plates 5-6: Green-orange (foreground ~100 / background ~30)
    // Further differentiate protan vs deutan
    TestPlate(
        id = 5,
        hiddenNumber = 7,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to null,
            ColorBlindnessType.DEUTERANOPIA to 7,
            ColorBlindnessType.PROTANOMALY to null,
            ColorBlindnessType.DEUTERANOMALY to 7,
            ColorBlindnessType.TRITANOPIA to 7,
            ColorBlindnessType.TRITANOMALY to 7,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 100f,
        backgroundHue = 30f,
        dotSeed = 1005L
    ),
    TestPlate(
        id = 6,
        hiddenNumber = 42,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 4,
            ColorBlindnessType.DEUTERANOPIA to 42,
            ColorBlindnessType.PROTANOMALY to 4,
            ColorBlindnessType.DEUTERANOMALY to 42,
            ColorBlindnessType.TRITANOPIA to 42,
            ColorBlindnessType.TRITANOMALY to 42,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 105f,
        backgroundHue = 28f,
        dotSeed = 1006L
    ),
    // Plates 7-8: Blue-yellow (foreground ~60 / background ~240)
    // Test tritan
    TestPlate(
        id = 7,
        hiddenNumber = 3,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 3,
            ColorBlindnessType.DEUTERANOPIA to 3,
            ColorBlindnessType.PROTANOMALY to 3,
            ColorBlindnessType.DEUTERANOMALY to 3,
            ColorBlindnessType.TRITANOPIA to null,
            ColorBlindnessType.TRITANOMALY to null,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 60f,
        backgroundHue = 240f,
        dotSeed = 1007L
    ),
    TestPlate(
        id = 8,
        hiddenNumber = 9,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 9,
            ColorBlindnessType.DEUTERANOPIA to 9,
            ColorBlindnessType.PROTANOMALY to 9,
            ColorBlindnessType.DEUTERANOMALY to 9,
            ColorBlindnessType.TRITANOPIA to null,
            ColorBlindnessType.TRITANOMALY to null,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 55f,
        backgroundHue = 235f,
        dotSeed = 1008L
    ),
    // Plates 9-10: Purple-blue (foreground ~270 / background ~220)
    // Further tritan testing
    TestPlate(
        id = 9,
        hiddenNumber = 4,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 4,
            ColorBlindnessType.DEUTERANOPIA to 4,
            ColorBlindnessType.PROTANOMALY to 4,
            ColorBlindnessType.DEUTERANOMALY to 4,
            ColorBlindnessType.TRITANOPIA to null,
            ColorBlindnessType.TRITANOMALY to 4,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 270f,
        backgroundHue = 220f,
        dotSeed = 1009L
    ),
    TestPlate(
        id = 10,
        hiddenNumber = 2,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 2,
            ColorBlindnessType.DEUTERANOPIA to 2,
            ColorBlindnessType.PROTANOMALY to 2,
            ColorBlindnessType.DEUTERANOMALY to 2,
            ColorBlindnessType.TRITANOPIA to null,
            ColorBlindnessType.TRITANOMALY to null,
            ColorBlindnessType.ACHROMATOPSIA to null
        ),
        foregroundHue = 275f,
        backgroundHue = 218f,
        dotSeed = 1010L
    ),
    // Plates 11-12: Control plates (achromatic, visible to all)
    // foregroundHue = 0, backgroundHue = 0, saturation distinction handled elsewhere
    TestPlate(
        id = 11,
        hiddenNumber = 7,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 7,
            ColorBlindnessType.DEUTERANOPIA to 7,
            ColorBlindnessType.PROTANOMALY to 7,
            ColorBlindnessType.DEUTERANOMALY to 7,
            ColorBlindnessType.TRITANOPIA to 7,
            ColorBlindnessType.TRITANOMALY to 7,
            ColorBlindnessType.ACHROMATOPSIA to 7
        ),
        foregroundHue = 0f,
        backgroundHue = 0f,
        dotSeed = 1011L
    ),
    TestPlate(
        id = 12,
        hiddenNumber = 5,
        answersByType = mapOf(
            ColorBlindnessType.PROTANOPIA to 5,
            ColorBlindnessType.DEUTERANOPIA to 5,
            ColorBlindnessType.PROTANOMALY to 5,
            ColorBlindnessType.DEUTERANOMALY to 5,
            ColorBlindnessType.TRITANOPIA to 5,
            ColorBlindnessType.TRITANOMALY to 5,
            ColorBlindnessType.ACHROMATOPSIA to 5
        ),
        foregroundHue = 0f,
        backgroundHue = 0f,
        dotSeed = 1012L
    )
)
