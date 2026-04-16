package com.sensecolor.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.sensecolor.app.data.datastore.PreferenceKeys
import com.sensecolor.app.data.model.ColorBlindnessType
import com.sensecolor.app.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { prefs ->
        UserPreferences(
            hasCompletedOnboarding = prefs[PreferenceKeys.HAS_COMPLETED_ONBOARDING] ?: false,
            colorBlindnessType = prefs[PreferenceKeys.COLOR_BLINDNESS_TYPE]?.let {
                try { ColorBlindnessType.valueOf(it) } catch (_: Exception) { ColorBlindnessType.NONE }
            } ?: ColorBlindnessType.NONE,
            hasCompletedTest = prefs[PreferenceKeys.HAS_COMPLETED_TEST] ?: false,
            textSizeScale = prefs[PreferenceKeys.TEXT_SIZE_SCALE] ?: 1.0f
        )
    }

    suspend fun setOnboardingComplete(type: ColorBlindnessType) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.HAS_COMPLETED_ONBOARDING] = true
            prefs[PreferenceKeys.COLOR_BLINDNESS_TYPE] = type.name
        }
    }

    suspend fun setColorBlindnessType(type: ColorBlindnessType) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.COLOR_BLINDNESS_TYPE] = type.name
        }
    }

    suspend fun setTestCompleted(type: ColorBlindnessType) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.HAS_COMPLETED_TEST] = true
            prefs[PreferenceKeys.COLOR_BLINDNESS_TYPE] = type.name
        }
    }

    suspend fun setTextSizeScale(scale: Float) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.TEXT_SIZE_SCALE] = scale
        }
    }

    suspend fun resetAll() {
        dataStore.edit { it.clear() }
    }
}
