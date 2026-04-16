package com.sensecolor.app.ui.screens.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sensecolor.app.data.model.ColorBlindnessType
import com.sensecolor.app.data.model.TestPlate
import com.sensecolor.app.data.model.defaultTestPlates
import com.sensecolor.app.data.repository.UserPreferencesRepository
import com.sensecolor.app.domain.TestScoringEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TestUiState(
    val plates: List<TestPlate> = defaultTestPlates,
    val currentPlateIndex: Int = 0,
    val answers: List<Int?> = emptyList(),
    val isComplete: Boolean = false,
    val result: ColorBlindnessType? = null
)

class TestViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

    fun onAnswer(answer: Int?) {
        val state = _uiState.value
        val newAnswers = state.answers + answer
        val nextIndex = state.currentPlateIndex + 1

        if (nextIndex >= state.plates.size) {
            // Test complete - score it
            val answerPairs = state.plates.zip(newAnswers).map { Pair(it.first, it.second) }
            val result = TestScoringEngine.score(answerPairs)
            _uiState.value = state.copy(
                answers = newAnswers,
                isComplete = true,
                result = result
            )
        } else {
            _uiState.value = state.copy(
                answers = newAnswers,
                currentPlateIndex = nextIndex
            )
        }
    }

    fun onAcceptResult(onNavigateToCamera: () -> Unit) {
        val result = _uiState.value.result ?: return
        viewModelScope.launch {
            preferencesRepository.setTestCompleted(result)
            preferencesRepository.setOnboardingComplete(result)
            onNavigateToCamera()
        }
    }
}
