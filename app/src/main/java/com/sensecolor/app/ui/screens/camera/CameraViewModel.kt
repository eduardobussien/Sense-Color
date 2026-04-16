package com.sensecolor.app.ui.screens.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

data class CameraUiState(
    val capturedPhotoUri: String? = null,
    val error: String? = null,
    val isCapturing: Boolean = false
)

class CameraViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun capturePhoto(context: Context, imageCapture: ImageCapture, onPhotoTaken: (String) -> Unit) {
        if (_uiState.value.isCapturing) return
        _uiState.value = _uiState.value.copy(isCapturing = true, error = null)

        val photoFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val uri = Uri.fromFile(photoFile).toString()
                    _uiState.value = _uiState.value.copy(
                        capturedPhotoUri = uri,
                        isCapturing = false
                    )
                    onPhotoTaken(uri)
                }

                override fun onError(exception: ImageCaptureException) {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to capture photo: ${exception.message}",
                        isCapturing = false
                    )
                }
            }
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
