package com.example.mindspotapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mindspotapp.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow

/**
 * Room Database Access Object (DAO) for MoodEntry
 * Ruh hali verileri için database işlemlerini yönetir
 */
@Dao
interface MoodDao {

    /**
     * Yeni ruh hali kaydı ekler
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(entry: MoodEntry)

    /**
     * Mevcut ruh hali kaydını günceller
     */
    @Update
    suspend fun updateMood(entry: MoodEntry)

    /**
     * Ruh hali kaydını siler
     */
    @Delete
    suspend fun deleteMood(entry: MoodEntry)

    /**
     * Tüm ruh hali kayıtlarını timestamp'e göre sıralı şekilde getirir
     * Flow kullanarak reactive data gözlemi sağlar
     */
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<MoodEntry>>

    /**
     * Belirli bir tarih aralığındaki ruh hali kayıtlarını getirir
     * @param startTime Başlangıç zamanı (Unix timestamp)
     * @param endTime Bitiş zamanı (Unix timestamp)
     */
    @Query("SELECT * FROM mood_entries WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getMoodsByDateRange(startTime: Long, endTime: Long): Flow<List<MoodEntry>>

    /**
     * Son N kayıtları getirir
     * @param limit Kayıt sayısı limiti
     */
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMoods(limit: Int): Flow<List<MoodEntry>>

    /**
     * Belirli bir ruh hali tipindeki kayıtların sayısını getirir
     * @param moodId Ruh hali ID'si
     */
    @Query("SELECT COUNT(*) FROM mood_entries WHERE moodId = :moodId")
    suspend fun getMoodCount(moodId: Int): Int

    /**
     * Belirli bir etikete sahip kayıtları getirir
     * @param tag Etiket adı
     */
    @Query("SELECT * FROM mood_entries WHERE optionalTag = :tag ORDER BY timestamp DESC")
    fun getMoodsByTag(tag: String): Flow<List<MoodEntry>>

    /**
     * Tüm verileri siler (test amaçlı)
     */
    @Query("DELETE FROM mood_entries")
    suspend fun deleteAllMoods()
}