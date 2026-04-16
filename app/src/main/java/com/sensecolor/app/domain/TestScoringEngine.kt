package com.sensecolor.app.domain

import com.sensecolor.app.data.model.ColorBlindnessType
import com.sensecolor.app.data.model.TestPlate

object TestScoringEngine {
    fun score(answers: List<Pair<TestPlate, Int?>>): ColorBlindnessType {
        val scores = mutableMapOf<ColorBlindnessType, Int>()
        ColorBlindnessType.entries.filter { it != ColorBlindnessType.NONE }.forEach { scores[it] = 0 }

        for ((plate, userAnswer) in answers) {
            // Normal vision should see the hiddenNumber
            val normalAnswer = plate.hiddenNumber

            if (userAnswer == normalAnswer) {
                // Correct answer - no deficiency signal
                continue
            }

            // Wrong answer - check which types would give this answer
            for ((type, expectedAnswer) in plate.answersByType) {
                if (type == ColorBlindnessType.NONE) continue
                if (userAnswer == expectedAnswer) {
                    scores[type] = (scores[type] ?: 0) + 1
                }
            }
        }

        val bestMatch = scores.maxByOrNull { it.value }
        return if (bestMatch != null && bestMatch.value >= 3) bestMatch.key else ColorBlindnessType.NONE
    }
}
