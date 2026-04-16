package com.sensecolor.app.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object PreferenceKeys {
    val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
    val COLOR_BLINDNESS_TYPE = stringPreferencesKey("color_blindness_type")
    val HAS_COMPLETED_TEST = booleanPreferencesKey("has_completed_test")
    val TEXT_SIZE_SCALE = floatPreferencesKey("text_size_scale")
}
