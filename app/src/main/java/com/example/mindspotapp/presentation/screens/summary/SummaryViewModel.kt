package com.example.mindspotapp.presentation.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindspotapp.data.model.MoodEntry
import com.example.mindspotapp.data.model.MoodType
import com.example.mindspotapp.domain.analytics.MoodAnalysisResult
import com.example.mindspotapp.domain.analytics.MoodAnalyticsEngine
import com.example.mindspotapp.domain.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Özet ekranının ViewModel'i
 * Ruh hali istatistiklerini ve geçmişi yönetir
 */
@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    private val analyticsEngine: MoodAnalyticsEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(SummaryUiState())
    val uiState: StateFlow<SummaryUiState> = _uiState.asStateFlow()

    init {
        loadMoodData()
    }

    /**
     * Ruh hali verilerini yükler
     */
    private fun loadMoodData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Son 30 günlük verileri al
                val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)
                val currentTime = System.currentTimeMillis()

                combine(
                    moodRepository.getAllMoods(),
                    moodRepository.getMoodsByDateRange(thirtyDaysAgo, currentTime)
                ) { allMoods, recentMoods ->
                    Pair(allMoods, recentMoods)
                }.collect { (allMoods, recentMoods) ->
                    val moodStats = calculateMoodStatistics(recentMoods)
                    val analysisResult = analyticsEngine.analyzeMoodData(allMoods)

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        allMoodEntries = allMoods,
                        recentMoodEntries = recentMoods,
                        moodStatistics = moodStats,
                        analysisResult = analysisResult,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Veriler yüklenirken hata oluştu: ${e.message}"
                )
            }
        }
    }

    /**
     * Ruh hali istatistiklerini hesaplar
     */
    private fun calculateMoodStatistics(entries: List<MoodEntry>): MoodStatistics {
        if (entries.isEmpty()) {
            return MoodStatistics()
        }

        val moodCounts = mutableMapOf<Int, Int>()
        entries.forEach { entry ->
            moodCounts[entry.moodId] = moodCounts.getOrDefault(entry.moodId, 0) + 1
        }

        val mostFrequentMoodId = moodCounts.maxByOrNull { it.value }?.key ?: 3
        val averageMood = entries.map { it.moodId }.average()

        return MoodStatistics(
            totalEntries = entries.size,
            averageMoodScore = averageMood.toFloat(),
            mostFrequentMood = MoodType.fromId(mostFrequentMoodId),
            moodDistribution = moodCounts,
            streak = calculateCurrentStreak(entries)
        )
    }

    /**
     * Günlük kayıt serisi hesaplar (streak)
     */
    private fun calculateCurrentStreak(entries: List<MoodEntry>): Int {
        if (entries.isEmpty()) return 0

        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        var streak = 0
        var currentDay = today.timeInMillis

        val entriesByDay = entries.groupBy { entry ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = entry.timestamp
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }

        while (entriesByDay.containsKey(currentDay)) {
            streak++
            currentDay -= 24 * 60 * 60 * 1000L // Bir gün geriye git
        }

        return streak
    }

    /**
     * Hata mesajını temizler
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

/**
 * Özet ekranının UI state'i
 */
data class SummaryUiState(
    val isLoading: Boolean = false,
    val allMoodEntries: List<MoodEntry> = emptyList(),
    val recentMoodEntries: List<MoodEntry> = emptyList(),
    val moodStatistics: MoodStatistics = MoodStatistics(),
    val analysisResult: MoodAnalysisResult = MoodAnalysisResult(),
    val errorMessage: String? = null
)

/**
 * Ruh hali istatistikleri
 */
data class MoodStatistics(
    val totalEntries: Int = 0,
    val averageMoodScore: Float = 0f,
    val mostFrequentMood: MoodType? = null,
    val moodDistribution: Map<Int, Int> = emptyMap(),
    val streak: Int = 0
)

/**
 * Tarih formatları için yardımcı fonksiyonlar
 */
object DateUtils {
    private val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }
}