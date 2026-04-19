package com.sensecolor.app.ui.screens.analysis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sensecolor.app.data.model.ColorBlindnessType
import com.sensecolor.app.data.model.TapPoint
import com.sensecolor.app.data.repository.UserPreferencesRepository
import com.sensecolor.app.domain.PhotoColorAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AnalysisUiState(
    val bitmap: Bitmap? = null,
    val selectedTapPointId: Int? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val colorBlindnessType: ColorBlindnessType = ColorBlindnessType.NONE,
    val sampleRadius: Int = 3
)

class AnalysisViewModel(
    private val photoUri: String,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalysisUiState())
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    val tapPoints = mutableStateListOf<TapPoint>()
    private var nextId = 0

    init {
        loadBitmap()
        observePreferences()
    }

    private fun loadBitmap() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uri = Uri.parse(photoUri)
                val path = uri.path ?: throw Exception("Invalid photo path")

                // First pass: get dimensions
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(path, options)

                // Calculate sample size to keep max dimension <= 2048
                val maxDim = maxOf(options.outWidth, options.outHeight)
                var sampleSize = 1
                while (maxDim / sampleSize > 2048) sampleSize *= 2

                // Second pass: decode
                val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
                val rawBitmap = BitmapFactory.decodeFile(path, decodeOptions)
                    ?: throw Exception("Failed to decode image")

                // Fix EXIF rotation
                val exif = ExifInterface(path)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                val rotation = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }
                val bitmap = if (rotation != 0f) {
                    val matrix = Matrix().apply { postRotate(rotation) }
                    Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
                        .also { if (it != rawBitmap) rawBitmap.recycle() }
                } else rawBitmap

                _uiState.value = _uiState.value.copy(bitmap = bitmap, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load image",
                    isLoading = false
                )
            }
        }
    }

    private fun observePreferences() {
        viewModelScope.launch {
            preferencesRepository.userPreferencesFlow.collect { prefs ->
                _uiState.value = _uiState.value.copy(colorBlindnessType = prefs.colorBlindnessType)
            }
        }
    }

    fun onImageTapped(normalizedX: Float, normalizedY: Float) {
        val bitmap = _uiState.value.bitmap ?: return
        if (normalizedX !in 0f..1f || normalizedY !in 0f..1f) return

        viewModelScope.launch(Dispatchers.Default) {
            val colorResult = PhotoColorAnalyzer.analyzePoint(
                bitmap = bitmap,
                normalizedX = normalizedX,
                normalizedY = normalizedY,
                sampleRadius = _uiState.value.sampleRadius,
                colorBlindnessType = _uiState.value.colorBlindnessType
            )
            val tapPoint = TapPoint(
                id = nextId++,
                normalizedX = normalizedX,
                normalizedY = normalizedY,
                colorResult = colorResult
            )
            tapPoints.add(tapPoint)
            _uiState.value = _uiState.value.copy(selectedTapPointId = tapPoint.id)
        }
    }

    fun onPinSelected(tapPoint: TapPoint) {
        _uiState.value = _uiState.value.copy(selectedTapPointId = tapPoint.id)
    }

    fun onDismissSheet() {
        _uiState.value = _uiState.value.copy(selectedTapPointId = null)
    }

    fun deleteTapPoint(id: Int) {
        tapPoints.removeAll { it.id == id }
        _uiState.value = _uiState.value.copy(selectedTapPointId = null)
    }

    fun clearAllPins() {
        tapPoints.clear()
        _uiState.value = _uiState.value.copy(selectedTapPointId = null)
    }

    fun setSampleRadius(radius: Int) {
        _uiState.value = _uiState.value.copy(sampleRadius = radius.coerceIn(1, 7))
    }
}
