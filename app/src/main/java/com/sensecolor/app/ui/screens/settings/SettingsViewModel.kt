package com.sensecolor.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sensecolor.app.data.model.ColorBlindnessType
import com.sensecolor.app.data.model.UserPreferences
import com.sensecolor.app.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val userPreferences: UserPreferences = UserPreferences(),
    val showTypeDialog: Boolean = false
)

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.userPreferencesFlow.collect { prefs ->
                _uiState.value = _uiState.value.copy(userPreferences = prefs)
            }
        }
    }

    fun showTypeDialog() {
        _uiState.value = _uiState.value.copy(showTypeDialog = true)
    }

    fun dismissTypeDialog() {
        _uiState.value = _uiState.value.copy(showTypeDialog = false)
    }

    fun onTypeChanged(type: ColorBlindnessType) {
        viewModelScope.launch {
            preferencesRepository.setColorBlindnessType(type)
            _uiState.value = _uiState.value.copy(showTypeDialog = false)
        }
    }

    fun onTextSizeChanged(scale: Float) {
        viewModelScope.launch {
            preferencesRepository.setTextSizeScale(scale)
        }
    }
}
