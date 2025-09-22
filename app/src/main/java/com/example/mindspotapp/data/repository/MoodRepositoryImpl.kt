package com.example.mindspotapp.data.repository

import com.example.mindspotapp.data.dao.MoodDao
import com.example.mindspotapp.data.model.MoodEntry
import com.example.mindspotapp.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * MoodRepository'nin implementasyonu
 * Room database üzerinden veri işlemlerini gerçekleştirir
 */
@Singleton
class MoodRepositoryImpl @Inject constructor(
    private val moodDao: MoodDao
) : MoodRepository {

    /**
     * Yeni ruh hali kaydı ekler
     */
    override suspend fun insertMoodEntry(moodId: Int, optionalTag: String?): Result<Unit> {
        return try {
            val entry = MoodEntry(
                moodId = moodId,
                timestamp = System.currentTimeMillis(),
                optionalTag = optionalTag
            )
            moodDao.insertMood(entry)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Tüm ruh hali kayıtlarını getirir
     */
    override fun getAllMoods(): Flow<List<MoodEntry>> {
        return moodDao.getAllMoods()
    }

    /**
     * Son N kayıtları getirir
     */
    override fun getRecentMoods(limit: Int): Flow<List<MoodEntry>> {
        return moodDao.getRecentMoods(limit)
    }

    /**
     * Belirli tarih aralığındaki kayıtları getirir
     */
    override fun getMoodsByDateRange(startTime: Long, endTime: Long): Flow<List<MoodEntry>> {
        return moodDao.getMoodsByDateRange(startTime, endTime)
    }

    /**
     * Belirli etiketteki kayıtları getirir
     */
    override fun getMoodsByTag(tag: String): Flow<List<MoodEntry>> {
        return moodDao.getMoodsByTag(tag)
    }

    /**
     * Ruh hali kaydını siler
     */
    override suspend fun deleteMoodEntry(entry: MoodEntry): Result<Unit> {
        return try {
            moodDao.deleteMood(entry)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}