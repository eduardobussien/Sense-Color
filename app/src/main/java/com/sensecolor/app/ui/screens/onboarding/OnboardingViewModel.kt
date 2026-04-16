package com.sensecolor.app.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sensecolor.app.data.model.ColorBlindnessType
import com.sensecolor.app.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val currentPage: Int = 0, // 0=welcome, 1=question, 2=type picker
    val selectedType: ColorBlindnessType? = null
)

class OnboardingViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onGetStarted() {
        _uiState.value = _uiState.value.copy(currentPage = 1)
    }

    fun onAnswerNo(onNavigateToCamera: () -> Unit) {
        viewModelScope.launch {
            preferencesRepository.setOnboardingComplete(ColorBlindnessType.NONE)
            onNavigateToCamera()
        }
    }

    fun onAnswerYes() {
        _uiState.value = _uiState.value.copy(currentPage = 2)
    }

    fun onSelectType(type: ColorBlindnessType) {
        _uiState.value = _uiState.value.copy(selectedType = type)
    }

    fun onConfirmType(onNavigateToCamera: () -> Unit) {
        val type = _uiState.value.selectedType ?: return
        viewModelScope.launch {
            preferencesRepository.setOnboardingComplete(type)
            onNavigateToCamera()
        }
    }

    fun onBack() {
        val current = _uiState.value.currentPage
        if (current > 0) {
            _uiState.value = _uiState.value.copy(currentPage = current - 1)
        }
    }
}
