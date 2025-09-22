package com.example.mindspotapp.presentation.screens.mood_entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindspotapp.domain.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Ruh hali kayıt ekranının ViewModel'i
 * UI state'ini yönetir ve repository ile iletişim kurar
 */
@HiltViewModel
class MoodEntryViewModel @Inject constructor(
    private val moodRepository: MoodRepository
) : ViewModel() {

    // UI State için private MutableStateFlow
    private val _uiState = MutableStateFlow(MoodEntryUiState())
    val uiState: StateFlow<MoodEntryUiState> = _uiState.asStateFlow()

    /**
     * Ruh hali kaydı yapar
     * @param moodId Seçilen ruh hali ID'si (1-5)
     * @param selectedTag Seçilen opsiyonel etiket
     */
    fun saveMoodEntry(moodId: Int, selectedTag: String? = null) {
        viewModelScope.launch {
            // Loading state'ini aktif et
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            // Repository'den kayıt işlemini gerçekleştir
            val result = moodRepository.insertMoodEntry(moodId, selectedTag)

            result.fold(
                onSuccess = {
                    // Başarılı kayıt
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSaved = true,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    // Hata durumu
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSaved = false,
                        errorMessage = "Kayıt başarısız: ${exception.message}"
                    )
                }
            )
        }
    }

    /**
     * UI state'ini sıfırlar
     */
    fun resetUiState() {
        _uiState.value = MoodEntryUiState()
    }

    /**
     * Error message'ı temizler
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

/**
 * Ruh hali kayıt ekranının UI state'i
 */
data class MoodEntryUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)