package com.example.mindspotapp.domain.repository

import com.example.mindspotapp.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow

/**
 * Ruh hali verilerini yönetmek için repository interface
 * Domain layer'da tanımlanır, data layer'da implement edilir
 */
interface MoodRepository {

    /**
     * Yeni ruh hali kaydı ekler
     * @param moodId Ruh hali ID'si (1-5)
     * @param optionalTag Opsiyonel etiket
     * @return İşlem sonucu
     */
    suspend fun insertMoodEntry(moodId: Int, optionalTag: String? = null): Result<Unit>

    /**
     * Tüm ruh hali kayıtlarını getirir
     * @return Flow olarak ruh hali listesi
     */
    fun getAllMoods(): Flow<List<MoodEntry>>

    /**
     * Son N kayıtları getirir
     * @param limit Kayıt sayısı
     * @return Flow olarak ruh hali listesi
     */
    fun getRecentMoods(limit: Int): Flow<List<MoodEntry>>

    /**
     * Belirli tarih aralığındaki kayıtları getirir
     * @param startTime Başlangıç zamanı
     * @param endTime Bitiş zamanı
     * @return Flow olarak ruh hali listesi
     */
    fun getMoodsByDateRange(startTime: Long, endTime: Long): Flow<List<MoodEntry>>

    /**
     * Belirli etiketteki kayıtları getirir
     * @param tag Etiket adı
     * @return Flow olarak ruh hali listesi
     */
    fun getMoodsByTag(tag: String): Flow<List<MoodEntry>>

    /**
     * Ruh hali kaydını siler
     * @param entry Silinecek kayıt
     */
    suspend fun deleteMoodEntry(entry: MoodEntry): Result<Unit>
}